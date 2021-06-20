package org.lixl.hadoop.lixlsource.metrics2.lib;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 可变指标估值接口
 * Created by lxl on 20/1/30.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public abstract class MutableGauge extends MutableMetric {
    private final MetricsInfo info;

    protected MutableGauge(MetricsInfo info) {
        this.info = checkNotNull(info, "指标信息");
    }

    protected MetricsInfo info() {
        return info;
    }

    /**
     * 按每次1增加值
     */
    public abstract void incr();

    /**
     * 按每次1减小值
     */
    public abstract void decr();
}
