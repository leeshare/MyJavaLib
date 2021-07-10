package org.lixl.hadoop.lixlsource.metrics2.impl;

import org.lixl.hadoop.lixlsource.metrics2.AbstractMetric;
import org.lixl.hadoop.lixlsource.metrics2.MetricType;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsVisitor;

class MetricGaugeLong extends AbstractMetric {
    final long value;

    MetricGaugeLong(MetricsInfo info, long value) {
        super(info);
        this.value = value;
    }

    @Override
    public Long value() {
        return value;
    }

    @Override
    public MetricType type() {
        return MetricType.GAUGE;
    }

    @Override
    public void visit(MetricsVisitor visitor) {
        visitor.gauge(this, value);
    }
}
