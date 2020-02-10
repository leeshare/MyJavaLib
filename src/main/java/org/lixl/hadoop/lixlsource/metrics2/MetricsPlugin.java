package org.lixl.hadoop.lixlsource.metrics2;

import org.apache.commons.configuration.SubsetConfiguration;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

/**
 * 插件接口 用于指标架构
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public interface MetricsPlugin {
    void init(SubsetConfiguration conf);
}
