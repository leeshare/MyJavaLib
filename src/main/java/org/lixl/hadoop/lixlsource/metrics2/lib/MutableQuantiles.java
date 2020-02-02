package org.lixl.hadoop.lixlsource.metrics2.lib;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang.StringUtils;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsRecordBuilder;
import org.lixl.hadoop.lixlsource.metrics2.util.Quantile;
import org.lixl.hadoop.lixlsource.metrics2.util.QuantileEstimator;
import org.lixl.hadoop.lixlsource.metrics2.util.SampleQuantiles;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.lixl.hadoop.lixlsource.metrics2.lib.Interns.info;

/**
 * 监控一个长值的流,维持在线评估特定 quantiles ————可能低错误
 * 这是特别有用的对于高精确度的潜在指标
 *
 * quantile - 分位数
 * Created by lxl on 20/1/30.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MutableQuantiles extends MutableMetric {

    @VisibleForTesting
    public static final Quantile[] quantiles = {
            new Quantile(0.50, 0.050),
            new Quantile(0.75, 0.025),
            new Quantile(0.90, 0.010),
            new Quantile(0.95, 0.005),
            new Quantile(0.99, 0.001)
    };

    private final MetricsInfo numInfo;
    private final MetricsInfo[] quantileInfos;
    private final int interval;

    private QuantileEstimator estimator;
    private long previousCount = 0;
    private ScheduledFuture scheduledTask = null;   //定时任务器

    @VisibleForTesting
    protected Map<Quantile, Long> previousSnapshot = null;

    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setDaemon(true).setNameFormat("MutableQuantiles-%d").build());

    public MutableQuantiles(String name, String description, String sampleName, String valueName, int interval) {
        String ucName = StringUtils.capitalize(name);
        String usName = StringUtils.capitalize(sampleName);
        String uvName = StringUtils.capitalize(valueName);
        String desc = StringUtils.uncapitalize(description);
        String lsName = StringUtils.uncapitalize(sampleName);
        String lvName = StringUtils.uncapitalize(valueName);

        numInfo = info(ucName + "Num" + usName, String.format("Number of %s for %s with %ds interval", lsName, desc, interval));
        quantileInfos = new MetricsInfo[quantiles.length];
        String nameTemplate = ucName + "%dthPercentile" + uvName;
        String descTemplate = "%d percentile " + lvName + " with " + interval + " second interval for " + desc;
        for(int i = 0; i < quantiles.length; i++) {
            int percentile = (int) (100 * quantiles[i].quantile);
            quantileInfos[i] = info(String.format(nameTemplate, percentile), String.format(descTemplate, percentile));
        }

        estimator = new SampleQuantiles(quantiles);
        this.interval = interval;
        scheduledTask = scheduler.scheduleAtFixedRate(new RolloverSample(this), interval, interval, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void snapshot(MetricsRecordBuilder builder, boolean all) {

    }

}
