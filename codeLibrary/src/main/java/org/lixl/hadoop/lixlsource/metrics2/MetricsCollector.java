package org.lixl.hadoop.lixlsource.metrics2;

/**
 * 指标收集者接口
 * Created by lxl on 20/1/21.
 */
public interface MetricsCollector {

    /**
     * 添加一个指标纪录
     * @param name
     * @return
     */
    public MetricsRecordBuilder addRecord(String name);

    /**
     * 添加一个指标纪录
     * @param info
     * @return
     */
    public MetricsRecordBuilder addRecord(MetricsInfo info);
}
