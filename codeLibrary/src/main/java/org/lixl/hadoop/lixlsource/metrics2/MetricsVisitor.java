package org.lixl.hadoop.lixlsource.metrics2;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

/**
 * 用于指标的 一个访问者接口
 * Created by lxl on 20/1/21.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public interface MetricsVisitor {

    /**
     * 回调用于整型估值
     *
     * @param info
     * @param value
     */
    public void gauge(MetricsInfo info, int value);

    public void gauge(MetricsInfo info, long value);

    public void gauge(MetricsInfo info, float value);

    public void gauge(MetricsInfo info, double value);

    /**
     * 回调用于整型的计数
     *
     * @param info
     * @param value
     */
    public void counter(MetricsInfo info, int value);

    public void counter(MetricsInfo info, long value);

}
