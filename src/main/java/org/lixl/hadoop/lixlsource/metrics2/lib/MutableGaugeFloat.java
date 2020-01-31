package org.lixl.hadoop.lixlsource.metrics2.lib;

import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsRecordBuilder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lxl on 20/1/30.
 */
public class MutableGaugeFloat extends MutableGauge {
    private AtomicInteger value = new AtomicInteger();

    MutableGaugeFloat(MetricsInfo info, float initValue) {
        super(info);
        this.value.set(Float.floatToIntBits(initValue));
    }

    public float value() {
        return Float.intBitsToFloat(value.get());
    }

    @Override
    public void incr() {
        incr(1.0f);
    }

    @Override
    public void decr() {
        incr(-1.0f);
    };

    @Override
    public void snapshot(MetricsRecordBuilder builder, boolean all) {
        if(all || changed()) {
            builder.addGauge(info(), value());
            clearChanged();
        }
    }

    public void set(float value) {
        this.value.set(Float.floatToIntBits(value));
        setChanged();
    }

    //比较 并 更新 CAS
    private final boolean compareAndSet(float expect, float update) {
        //AtomicInteger compareAndSet 是原子操作
        //参数1 是当前内存值 current; 参数2 是预期要修改成的值 new
        //AtomicInteger对象地址的真实值 real 和 current 做比较.
        // 如果比较相等,说明real未被修改过,则将new赋值给real结束,返回true
        // 如果比较不等,说明real已被修改过,则结束,将new重新赋值给real
        return value.compareAndSet(Float.floatToIntBits(expect), Float.floatToIntBits(update));
    }

    //这个增加的方法太牛了
    private void incr(float delta) {
        while(true) {
            float current = value.get();
            float next = current + delta;
            // 这个 current 和 next 怎么可能相等呢 ???
            if(compareAndSet(current, next)) {
                setChanged();
                return;
            }
        }
    }

    public String toString() {
        return value.toString();
    }

}
