package org.lixl.hadoop.lixlsource.metrics2.lib;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsRecordBuilder;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lxl on 20/1/30.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MutableGaugeLong extends MutableGauge {
    private AtomicLong value = new AtomicLong();

    MutableGaugeLong(MetricsInfo info, long initValue) {
        super(info);
        this.value.set(initValue);
    }

    public long value() {
        return value.get();
    }

    @Override
    public void incr() {
        incr(1);
    }

    public void incr(long delta) {
        value.addAndGet(delta);
        setChanged();
    }

    @Override
    public void decr() {
        decr(1);
    }

    public void decr(long delta) {
        this.value.addAndGet(-delta);
        setChanged();
    }

    public void set(long delta) {
        this.value.set(delta);
        setChanged();
    }

    @Override
    public void snapshot(MetricsRecordBuilder builder, boolean all) {
        if (all || changed()) {
            builder.addGauge(info(), value());
            clearChanged();
        }
    }

    public String toString() {
        return value.toString();
    }

}
