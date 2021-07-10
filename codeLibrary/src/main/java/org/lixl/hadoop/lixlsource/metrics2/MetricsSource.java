package org.lixl.hadoop.lixlsource.metrics2;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

/**
 * 指标信息的 源。 它生产并更新指标。它用指标系统注册，周期性提取它收集和发送它给 MetricsSink
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public interface MetricsSource {
    /**
     * 从指标源 获取指标
     *
     * @param collector
     * @param all
     */
    void getMetrics(MetricsCollector collector, boolean all);
}
