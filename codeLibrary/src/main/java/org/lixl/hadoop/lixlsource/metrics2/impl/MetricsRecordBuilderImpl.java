package org.lixl.hadoop.lixlsource.metrics2.impl;

import com.google.common.collect.Lists;
import org.lixl.hadoop.lixlsource.metrics2.*;
import org.lixl.hadoop.lixlsource.metrics2.lib.Interns;
import org.lixl.hadoop.lixlsource.util.Time;

import java.util.Collections;
import java.util.List;

/**
 * 允许对指标配置过滤器
 */
class MetricsRecordBuilderImpl extends MetricsRecordBuilder {
    private final MetricsCollector parent;
    private final long timestamp;
    private final MetricsInfo recInfo;
    private final List<AbstractMetric> metrics;
    private final List<MetricsTag> tags;
    private final MetricsFilter recordFilter, metricFilter;
    private final boolean acceptable;

    MetricsRecordBuilderImpl(MetricsCollector parent, MetricsInfo info,
                             MetricsFilter rf, MetricsFilter mf, boolean acceptable) {
        this.parent = parent;
        timestamp = Time.now();
        recInfo = info;
        metrics = Lists.newArrayList();
        tags = Lists.newArrayList();
        recordFilter = rf;
        metricFilter = mf;
        this.acceptable = acceptable;
    }

    @Override
    public MetricsCollector parent() {
        return parent;
    }

    @Override
    public MetricsRecordBuilderImpl tag(MetricsInfo info, String value) {
        if (acceptable) {
            tags.add(Interns.tag(info, value));
        }
        return this;
    }

    @Override
    public MetricsRecordBuilderImpl add(MetricsTag tag) {
        tags.add(tag);
        return this;
    }

    @Override
    public MetricsRecordBuilderImpl add(AbstractMetric metric) {
        metrics.add(metric);
        return this;
    }

    @Override
    public MetricsRecordBuilderImpl addCounter(MetricsInfo info, int value) {
        if (acceptable && (metricFilter == null || metricFilter.accepts(info.name()))) {
            metrics.add(new MetricCounterInt(info, value));
        }
        return this;
    }

    @Override
    public MetricsRecordBuilderImpl addCounter(MetricsInfo info, long value) {
        if (acceptable && (metricFilter == null || metricFilter.accepts(info.name()))) {
            metrics.add(new MetricCounterLong(info, value));
        }
        return this;
    }

    @Override
    public MetricsRecordBuilderImpl addGauge(MetricsInfo info, int value) {
        if (acceptable && (metricFilter == null || metricFilter.accepts(info.name()))) {
            metrics.add(new MetricGaugeInt(info, value));
        }
        return this;
    }

    @Override
    public MetricsRecordBuilderImpl addGauge(MetricsInfo info, long value) {
        if (acceptable && (metricFilter == null || metricFilter.accepts(info.name()))) {
            metrics.add(new MetricGaugeLong(info, value));
        }
        return this;
    }

    @Override
    public MetricsRecordBuilderImpl addGauge(MetricsInfo info, float value) {
        if (acceptable && (metricFilter == null || metricFilter.accepts(info.name()))) {
            metrics.add(new MetricGaugeFloat(info, value));
        }
        return this;
    }

    @Override
    public MetricsRecordBuilderImpl addGauge(MetricsInfo info, double value) {
        if (acceptable && (metricFilter == null || metricFilter.accepts(info.name()))) {
            metrics.add(new MetricGaugeDouble(info, value));
        }
        return this;
    }

    @Override
    public MetricsRecordBuilderImpl setContext(String value) {
        return tag(MsInfo.Context, value);
    }

    public MetricsRecordImpl getRecord() {
        if (acceptable && (recordFilter == null || recordFilter.accepts(tags))) {
            return new MetricsRecordImpl(recInfo, timestamp, tags(), metrics);
        }
        return null;
    }

    List<MetricsTag> tags() {
        return Collections.unmodifiableList(tags);
    }

    List<AbstractMetric> metrics() {
        return Collections.unmodifiableList(metrics);
    }

}
