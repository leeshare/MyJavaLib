package org.lixl.hadoop.lixlsource.metrics2.lib;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsRecordBuilder;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 一个可变长整型计数器
 * Created by lxl on 20/1/30.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MutableCounterLong extends MutableCounter {
    private AtomicLong value = new AtomicLong();

    public MutableCounterLong(MetricsInfo info, long initValue) {
        super(info);
        this.value.set(initValue);
    }

    @Override
    public void incr() {
        incr(1);
    }

    /**
     * 这里为何不加上关键字 synchronized ?
     * @param delta
     */
    public void incr(long delta) {
        value.addAndGet(delta);
        setChanged();
    }

    public long value() {
        return value.get();
    }

    @Override
    public void snapshot(MetricsRecordBuilder builder, boolean all) {
        if(all || changed()) {
            builder.addCounter(info(), value());
            clearChanged();
        }
    }

}
