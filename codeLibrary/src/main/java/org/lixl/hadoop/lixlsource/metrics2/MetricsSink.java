package org.lixl.hadoop.lixlsource.metrics2;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

/**
 * 指标下沉 接口
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public interface MetricsSink extends MetricsPlugin {

    void putMetrics(MetricsRecord record);

    void flush();
}
