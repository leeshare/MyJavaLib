package org.lixl.hadoop.lixlsource.metrics2.impl;

import com.google.common.collect.Maps;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.metrics2.MetricsCollector;
import org.lixl.hadoop.lixlsource.metrics2.MetricsSink;
import org.lixl.hadoop.lixlsource.metrics2.MetricsSource;
import org.lixl.hadoop.lixlsource.metrics2.MetricsSystem;
import org.lixl.hadoop.lixlsource.metrics2.annotation.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 一个基本类 用于 指标系统 单实例
 */
@InterfaceAudience.Private
@Metrics(context="metricssystem")
public class MetricsSystemImpl extends MetricsSystem implements MetricsSource {
    static final Logger LOG = LoggerFactory.getLogger(MetricsSystemImpl.class);
    static final String MS_NAME = "MetricsSystem";
    static final String MS_STATS_NAME = MS_NAME + ",sub=Stats";
    static final String MS_STATS_DES = "Metrics system metrics";
    static final String MS_CONTROL_NAME = MS_NAME + ",sub=Control";
    static final String MS_INIT_MODE_KEY = "hadoop.metrics.init.mode";

    @Override
    public void getMetrics(MetricsCollector collector, boolean all) {

    }

    @Override
    public MetricsSystem init(String prefix) {
        return null;
    }

    @Override
    public <T> T register(String name, String desc, T source) {
        return null;
    }

    @Override
    public void unregisterSource(String name) {

    }

    @Override
    public MetricsSource getSource(String name) {
        return null;
    }

    @Override
    public <T extends MetricsSink> T register(String name, String desc, T sink) {
        return null;
    }

    @Override
    public void register(Callback callback) {

    }

    @Override
    public void publishMetricsNow() {

    }

    @Override
    public boolean shutdown() {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void startMetricsMBeans() {

    }

    @Override
    public void stopMetricsMBeans() {

    }

    @Override
    public String currentConfig() {
        return null;
    }

    enum InitMode { NORMAL, STANDBY }

    private final Map<String, MetricsSourceAdapter> sources;

    public MetricsSystemImpl(String prefix) {
        /*this.prefix = prefix;
        allSources = Maps.newHashMap();
        sources = Maps.newLinkedHashMap();
        allSinks = Maps.newHashMap();
        sinks = Maps.newLinkedHashMap();
        sourceConfigs = Maps.newHashMap();
        sinkConfigs = Maps.newHashMap();
        callbacks = Lists.newArrayList();
        namedCallbacks = Maps.newHashMap();
        injectedTags = Lists.newArrayList();
        collector = new MetricsCollectorImpl();
        if (prefix != null) {
            // prefix could be null for default ctor, which requires init later
            initSystemMBean();
        }*/
        sources = Maps.newLinkedHashMap();
    }

    public MetricsSystemImpl() {
        this(null);
    }
}
