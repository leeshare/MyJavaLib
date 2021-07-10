package org.lixl.hadoop.lixlsource.metrics2.lib;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsRecordBuilder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lxl on 20/1/30.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MutableGaugeInt extends MutableGauge {
    private AtomicInteger value = new AtomicInteger();

    MutableGaugeInt(MetricsInfo info, int initValue) {
        super(info);
        this.value.set(initValue);
    }

    public int value() {
        return value.get();
    }

    @Override
    public void incr() {
        incr(1);
    }

    public void incr(int delta) {
        value.addAndGet(delta);
        setChanged();
    }

    @Override
    public void decr() {
        decr(1);
    }

    public void decr(int delta) {
        value.addAndGet(-delta);
        setChanged();
    }

    public void set(int value) {
        this.value.set(value);
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
