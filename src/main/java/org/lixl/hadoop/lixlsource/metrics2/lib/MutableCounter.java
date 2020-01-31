package org.lixl.hadoop.lixlsource.metrics2.lib;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;

/**
 * 可变计数器指标接口
 * Created by lxl on 20/1/30.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public abstract class MutableCounter extends MutableMetric {
    private final MetricsInfo info;

    protected MutableCounter(MetricsInfo info) {
        this.info = info;
    }

    protected MetricsInfo info() {
        return info;
    }

    /**
     * 按每次1 增加指标的值
     */
    public abstract void incr();
}
