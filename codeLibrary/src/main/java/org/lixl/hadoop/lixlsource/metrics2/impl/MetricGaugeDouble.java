package org.lixl.hadoop.lixlsource.metrics2.impl;

import org.lixl.hadoop.lixlsource.metrics2.AbstractMetric;
import org.lixl.hadoop.lixlsource.metrics2.MetricType;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsVisitor;

class MetricGaugeDouble extends AbstractMetric {
    final double value;

    MetricGaugeDouble(MetricsInfo info, double value) {
        super(info);
        this.value = value;
    }

    @Override
    public Double value(){
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
