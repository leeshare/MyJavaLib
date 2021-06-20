package org.lixl.hadoop.lixlsource.metrics2;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

/**
 * 指标的纪录建造者接口
 * Created by lxl on 20/1/21.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public abstract class MetricsRecordBuilder {
    /**
     * 用指标信息 添加一个指标值
     * @param info
     * @param value
     * @return
     */
    public abstract MetricsRecordBuilder tag(MetricsInfo info, String value);

    /**
     * 添加一个不可变指标标签对象
     * @param tag
     * @return
     */
    public abstract MetricsRecordBuilder add(MetricsTag tag);

    /**
     * 添加一个 pre-made 不可变指标对象
     * @param metric
     * @return
     */
    public abstract MetricsRecordBuilder add(AbstractMetric metric);

    /**
     * 设置上下文标签
     * @param value
     * @return
     */
    public abstract MetricsRecordBuilder setContext(String value);

    /**
     * 添加一个整型指标
     * @param info
     * @param value
     * @return
     */
    public abstract MetricsRecordBuilder addCounter(MetricsInfo info, int value);

    /**
     * 添加一个long指标
     * @param info
     * @param value
     * @return
     */
    public abstract MetricsRecordBuilder addCounter(MetricsInfo info, long value);

    /**
     * 添加一个整型估值指标
     * @param info
     * @param value
     * @return
     */
    public abstract MetricsRecordBuilder addGauge(MetricsInfo info, int value);

    /**
     * 添加一个long估值指标
     * @param info
     * @param value
     * @return
     */
    public abstract MetricsRecordBuilder addGauge(MetricsInfo info, long value);

    /**
     * 添加一个float估值指标
     * @param info
     * @param value
     * @return
     */
    public abstract MetricsRecordBuilder addGauge(MetricsInfo info, float value);

    /**
     * 添加一个double估值指标
     * @param info
     * @param value
     * @return
     */
    public abstract MetricsRecordBuilder addGauge(MetricsInfo info, double value);

    /**
     * @return 父指标收集器对象
     */
    public abstract MetricsCollector parent();

    /**
     * 在一个收集器中添加多条纪录到一个 liner 的语法糖
     * @return
     */
    public MetricsCollector endRecord() {
        return parent();
    }

}
