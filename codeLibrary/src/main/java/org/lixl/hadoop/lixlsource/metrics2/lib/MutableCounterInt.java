package org.lixl.hadoop.lixlsource.metrics2.lib;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsRecordBuilder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一个可变整型计数器 用于 实现指标源
 * Created by lxl on 20/1/30.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MutableCounterInt extends MutableCounter {
    private AtomicInteger value = new AtomicInteger();

    MutableCounterInt(MetricsInfo info, int initValue) {
        super(info);
        this.value.set(initValue);
    }

    @Override
    public void incr() {
        incr(1);
    }

    public synchronized void incr(int delta) {
        value.addAndGet(delta);
        setChanged();
    }

    public int value() {
        return value.get();
    }

    @Override
    public void snapshot(MetricsRecordBuilder builder, boolean all) {
        if (all || changed()) {
            builder.addCounter(info(), value());
            clearChanged();
        }
    }
}
