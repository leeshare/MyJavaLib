package org.lixl.hadoop.lixlsource.metrics2;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

/**
 * JMX接口 用于指标系统
 * (Java Management Extensions，即Java管理扩展)
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public interface MetricsSystemMXBean {
    /**
     * 启动指标系统
     */
    public void start();

    /**
     * 停止指标系统
     */
    public void stop();

    /**
     * 启动指标 MBeans
     */
    public void startMetricsMBeans();

    /**
     * 停止指标 MBeans
     */
    public void stopMetricsMBeans();

    public String currentConfig();
}
