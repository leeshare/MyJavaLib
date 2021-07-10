package org.lixl.hadoop.lixlsource.metrics2.lib;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsRecordBuilder;

/**
 * 可变指标接口
 * Created by lxl on 20/1/21.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public abstract class MutableMetric {
    private volatile boolean changed = true;

    /**
     * 获取一个指标快照
     *
     * @param builder
     * @param all
     */
    public abstract void snapshot(MetricsRecordBuilder builder, boolean all);

    /**
     * 若发生改变,则获取一个指标快照
     *
     * @param builder
     */
    public void snapshot(MetricsRecordBuilder builder) {
        snapshot(builder, false);
    }

    /**
     * 在可变操作中,设置变更标识
     */
    protected void setChanged() {
        changed = true;
    }

    /**
     * 在快照操作中,清除变更标识
     */
    protected void clearChanged() {
        changed = false;
    }

    /**
     * @return true 如果自上一次快照,指标发生改变
     */
    public boolean changed() {
        return changed;
    }
}
