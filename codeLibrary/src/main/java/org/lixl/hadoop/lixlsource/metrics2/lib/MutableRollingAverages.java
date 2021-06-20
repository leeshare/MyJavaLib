package org.lixl.hadoop.lixlsource.metrics2.lib;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sun.istack.Nullable;
import org.apache.commons.lang.StringUtils;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsRecordBuilder;
import org.lixl.hadoop.lixlsource.metrics2.impl.MetricsCollectorImpl;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;

import static org.lixl.hadoop.lixlsource.metrics2.lib.Interns.info;

/**
 * 此类维护一组滚动的平均指标
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MutableRollingAverages extends MutableMetric implements Closeable {
    private MutableRatesWithAggregation innerMetrics = new MutableRatesWithAggregation();
    @VisibleForTesting
    static final ScheduledExecutorService SCHEDULER =
            Executors.newScheduledThreadPool(1,
                    new ThreadFactoryBuilder().setDaemon(true).setNameFormat("MutableRollingAverages-%d").build());
    private ScheduledFuture<?> scheduledTask = null;

    @Nullable
    private Map<String, MutableRate> currentSnapshot;

    private final String avgInfoNameTemplate;
    private final String avgInfoDescTemplate;
    private int numWindows;

    private static class SumAndCount {
        private final double sum;
        private final long count;

        SumAndCount(final double sum, final long count) {
            this.sum = sum;
            this.count = count;
        }

        public double getSum() {
            return sum;
        }
        public long getCount() {
            return count;
        }
    }

    private Map<String, LinkedBlockingDeque<SumAndCount>> averages = new ConcurrentHashMap<>();

    private static final long WINDOW_SIZE_MS_DEFAULT = 300_000; //就是 300000 加下划线就为增强可读性。
    private static final int NUM_WINDOWS_DEFAULT = 36;

    public MutableRollingAverages(String metricValueName) {
        if(metricValueName == null) {
            metricValueName = "";
        }
        avgInfoNameTemplate = "[%s]RollingAvg" + StringUtils.capitalize(metricValueName);
        avgInfoDescTemplate = "Rolling average" + StringUtils.uncapitalize(metricValueName) + " for %s";
        numWindows = NUM_WINDOWS_DEFAULT;
        scheduledTask = SCHEDULER.scheduleAtFixedRate(new RatesRoller(this),
                WINDOW_SIZE_MS_DEFAULT, WINDOW_SIZE_MS_DEFAULT, TimeUnit.MILLISECONDS);
    }

    @VisibleForTesting
    synchronized void replaceScheduledTask(int windows, long interval, TimeUnit timeUnit) {
        numWindows = windows;
        scheduledTask.cancel(true);
        scheduledTask = SCHEDULER.scheduleAtFixedRate(new RatesRoller(this), interval, interval, timeUnit);
    }

    @Override
    public void snapshot(MetricsRecordBuilder builder, boolean all) {
        if(all || changed()) {
            for(final Map.Entry<String, LinkedBlockingDeque<SumAndCount>> entry : averages.entrySet()) {
                final String name = entry.getKey();
                final MetricsInfo avgInfo = info(
                        String.format(avgInfoNameTemplate, StringUtils.capitalize(name)),
                        String.format(avgInfoDescTemplate, StringUtils.uncapitalize(name)));
                double totalSum = 0;
                long totalCount = 0;

                if(totalCount != 0) {
                    builder.addGauge(avgInfo, totalSum / totalCount);
                }
            }
            if(changed()) {
                clearChanged();
            }
        }
    }

    public void collectThreadLocalStates() {
        innerMetrics.collectThreadLocalStates();
    }

    public void add(final String name, final long value) {
        innerMetrics.add(name, value);
    }

    private static class RatesRoller implements Runnable {
        private final MutableRollingAverages parent;

        RatesRoller(final MutableRollingAverages parent) {
            this.parent = parent;
        }

        @Override
        public void run() {
            synchronized (parent) {
                final MetricsCollectorImpl mc = new MetricsCollectorImpl();
                final MetricsRecordBuilder rb = mc.addRecord("RatesRoller");
                /**
                 * 快照所有指标无论是否改变，在案例中，若自从上一快照没有改变，则得到0
                 */
                parent.innerMetrics.snapshot(rb, true);
                Preconditions.checkState(mc.getRecords().size() == 1, "一定只有一条记录叫'RatesRoller'");
                parent.currentSnapshot = parent.innerMetrics.getGlobalMetrics();
                parent.rollOverAvgs();
            }
            parent.setChanged();
        }
    }

    private synchronized void rollOverAvgs() {
        if(currentSnapshot == null) {
            return;
        }
        for(Map.Entry<String, MutableRate> entry : currentSnapshot.entrySet()) {
            final MutableRate rate = entry.getValue();
            final LinkedBlockingDeque<SumAndCount> deque = averages.computeIfAbsent(
                    entry.getKey(),
                    new Function<String, LinkedBlockingDeque<SumAndCount>>() {
                        @Override
                        public LinkedBlockingDeque<SumAndCount> apply(String k) {
                            return new LinkedBlockingDeque<>(numWindows);
                        }
                    }
            );
            final SumAndCount sumAndCount = new SumAndCount(rate.lastStat().total(), rate.lastStat().numSamples());
            if(!deque.offerLast(sumAndCount)) {
                deque.pollFirst();
                deque.offerLast(sumAndCount);
            }
        }
        setChanged();
    }

    @Override
    public void close() throws IOException {
        if(scheduledTask != null) {
            scheduledTask.cancel(false);
        }
        scheduledTask = null;
    }

    /**
     * 取回一个指标名称的map -> 总数
     * 过滤出 不包含至少最小样例的 实体
     * @param minSamples
     * @return
     */
    public synchronized Map<String, Double> getStats(long minSamples) {
        final Map<String, Double> stats = new HashMap<>();
        for(final Map.Entry<String, LinkedBlockingDeque<SumAndCount>> entry : averages.entrySet()) {
            final String name = entry.getKey();
            double totalSum = 0;
            long totalCount = 0;

            for(final SumAndCount sumAndCount : entry.getValue()) {
                totalCount += sumAndCount.getCount();
                totalSum += sumAndCount.getSum();
            }

            if(totalCount > minSamples) {
                stats.put(name, totalSum / totalCount);
            }
        }
        return stats;
    }

}
