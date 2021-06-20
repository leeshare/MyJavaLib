package org.lixl.hadoop.lixlsource.metrics2.lib;

import com.google.common.collect.Sets;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsRecordBuilder;
import org.lixl.hadoop.lixlsource.metrics2.util.SampleStat;
import org.mortbay.log.Log;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;

/**
 * 可变速率及总计： 帮助类 管理一组可变 速率指标 (rate metrics)
 *
 * 每个线程将维持一个本地速率计数、快照，这些值将和计入总速率。
 * 此类应仅被用于长期运行的线程，比如一些指标生成在最后的快照和线程死亡间将丢失。
 * 这考虑到有重大意义的高并发。
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MutableRatesWithAggregation extends MutableMetric {
    //static final Logger LOG = LoggerFactory.getLogger(MutableRatesWithAggregation.class);
    private final Map<String, MutableRate> globalMetrics = new ConcurrentHashMap<>();
    private final Set<Class<?>> protocolCache = Sets.newHashSet();

    //ConcurrentLinkedDeque 双向链表
    //WeakReference 弱引用（当一个对象仅被弱引用指向时，如果这时GC运行，那么这个对象就会被回收，而不论当前内存空间是否足够）
    private final ConcurrentLinkedDeque<WeakReference<ConcurrentMap<String, ThreadSafeSampleStat>>>
        weakReferenceQueue = new ConcurrentLinkedDeque<>();
    private final ThreadLocal<ConcurrentMap<String, ThreadSafeSampleStat>>
        threadLocalMetricsMap = new ThreadLocal<>();

    public void init(Class<?> protocol) {
        if(protocolCache.contains(protocol)) {
            return;
        }
        protocolCache.add(protocol);
        for(Method method : protocol.getDeclaredMethods()) {
            String name = method.getName();
            Log.debug(name);
            addMetricIfNotExists(name);
        }
    }

    public void add(String name, long elapsed) {
        ConcurrentMap<String, ThreadSafeSampleStat> localStats = threadLocalMetricsMap.get();
        if(localStats == null) {
            localStats = new ConcurrentHashMap<>();
            threadLocalMetricsMap.set(localStats);
            weakReferenceQueue.add(new WeakReference<>(localStats));
        }
        ThreadSafeSampleStat stat = localStats.get(name);
        if(stat == null) {
            stat = new ThreadSafeSampleStat();
            localStats.put(name, stat);
        }
        stat.add(elapsed);
    }

    @Override
    public synchronized void snapshot(MetricsRecordBuilder rb, boolean all) {
        Iterator<WeakReference<ConcurrentMap<String, ThreadSafeSampleStat>>> iter = weakReferenceQueue.iterator();
        while(iter.hasNext()) {
            ConcurrentMap<String, ThreadSafeSampleStat> map = iter.next().get();
            if(map == null) {
                iter.remove();
            } else {
                aggregateLocalStatesToGlobalMetrics(map);
            }
        }
        for(MutableRate globalMetric : globalMetrics.values()) {
            globalMetric.snapshot(rb, all);
        }
    }

    synchronized void collectThreadLocalStates() {
        final ConcurrentMap<String, ThreadSafeSampleStat> localStats = threadLocalMetricsMap.get();
        if(localStats != null) {
            aggregateLocalStatesToGlobalMetrics(localStats);
        }
    }

    private void aggregateLocalStatesToGlobalMetrics(final ConcurrentMap<String, ThreadSafeSampleStat> localStats) {
        for(Map.Entry<String, ThreadSafeSampleStat> entry : localStats.entrySet()) {
            String name = entry.getKey();
            MutableRate globalMetric = addMetricIfNotExists(name);
            entry.getValue().snapshotInfo(globalMetric);
        }
    }

    Map<String, MutableRate> getGlobalMetrics() {
        return globalMetrics;
    }

    private synchronized MutableRate addMetricIfNotExists(String name) {
        MutableRate metric = globalMetrics.get(name);
        if(metric == null) {
            metric = new MutableRate(name, name, false);
            globalMetrics.put(name, metric);
        }
        return metric;
    }



    private static class ThreadSafeSampleStat {
        private SampleStat stat = new SampleStat();
        synchronized void add(double x) {
            stat.add(x);
        }
        synchronized void snapshotInfo(MutableRate metric) {
            if(stat.numSamples() > 0) {
                metric.add(stat.numSamples(), Math.round(stat.total()));
                stat.reset();
            }
        }
    }
}
