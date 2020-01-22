package org.lixl.hadoop.lixlsource.metrics2;

/**
 * Created by lxl on 20/1/21.
 */
public enum MetricType {

    /**
     * 一个单独的增加指标, 用于计算吞吐量
     */
    COUNTER,

    /**
     * 一个随意的变化指标
     */
    GAUGE
}
