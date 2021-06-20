package org.lixl.hadoop.lixlsource.metrics2.impl;

import org.lixl.hadoop.lixlsource.metrics2.AbstractMetric;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsTag;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.lixl.hadoop.lixlsource.metrics2.util.Contracts.checkArg;

class MetricsRecordImpl extends AbstractMetricsRecord {
    protected static final String DEFAULT_CONTEXT = "default";

    private final long timestamp;
    private final MetricsInfo info;
    private final List<MetricsTag> tags;
    private final Iterable<AbstractMetric> metrics;

    public MetricsRecordImpl(MetricsInfo info, long timestamp,
                             List<MetricsTag> tags, Iterable<AbstractMetric> metrics) {
        this.timestamp = checkArg(timestamp, timestamp > 0, "timestamp");
        this.info = checkNotNull(info, "info");
        this.tags = checkNotNull(tags, "tags");
        this.metrics = checkNotNull(metrics, "metrics");
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    public String name() {
        return info.name();
    }

    MetricsInfo info() {
        return info;
    }

    @Override
    public String description() {
        return info.description();
    }

    @Override
    public String context() {
        for(MetricsTag t : tags) {
            if(t.info() == MsInfo.Context) {
                return t.value();
            }
        }
        return DEFAULT_CONTEXT;
    }

    @Override
    public List<MetricsTag> tags() {
        return tags;
    }

    @Override
    public Iterable<AbstractMetric> metrics() {
        return metrics;
    }

}
