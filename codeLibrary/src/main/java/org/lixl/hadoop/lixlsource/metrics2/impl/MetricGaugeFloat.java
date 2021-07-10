package org.lixl.hadoop.lixlsource.metrics2.impl;

import org.lixl.hadoop.lixlsource.metrics2.AbstractMetric;
import org.lixl.hadoop.lixlsource.metrics2.MetricType;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsVisitor;

class MetricGaugeFloat extends AbstractMetric {
    final float value;

    MetricGaugeFloat(MetricsInfo info, float value) {
        super(info);
        this.value = value;
    }

    @Override
    public Float value() {
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
