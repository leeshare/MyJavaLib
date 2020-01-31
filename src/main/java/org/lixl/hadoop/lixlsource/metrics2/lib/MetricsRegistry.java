package org.lixl.hadoop.lixlsource.metrics2.lib;

import com.google.common.collect.Maps;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsTag;

import java.util.Map;

/**
 * Created by lxl on 20/1/21.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MetricsRegistry {
    private final Map<String, MutableMetric> metricsMap = Maps.newLinkedHashMap();
    private final Map<String, MetricsTag> tagsMap = Maps.newLinkedHashMap();
    private final MetricsInfo metricsInfo;

    public MetricsRegistry(String name) {
        metricsInfo = Interns.info(name, name);
    }

    public MetricsRegistry(MetricsInfo info) {
        metricsInfo = info;
    }

    public MetricsInfo info() {
        return metricsInfo;
    }

    public synchronized MutableMetric get(String name) {
        return metricsMap.get(name);
    }

    public synchronized MetricsTag getTag(String name) {
        return tagsMap.get(name);
    }

    /**
     * 创建一个可变的整型计数器
     * @param name
     * @param desc
     * @param iVal
     * @return
     */
    public MutableCounterInt newCounter(String name, String desc, int iVal) {
        return newCounter(Interns.info(name, desc), iVal);
    }

    /**
     * 创建一个可变的整型计数器
     * @param info
     * @param iVal
     * @return
     */
    public synchronized MutableCounterInt newCounter(MetricsInfo info, int iVal) {
        checkMetricName(info.name());
        MutableCounterInt ret = new MutableCounterInt(info, iVal);
        metricsMap.put(info.name(), ret);
        return ret;
    }

    /**
     * 创建一个可变long计数器
     * @param name
     * @param desc
     * @param iVal
     * @return
     */
    public MutableCounterLong newCounter(String name, String desc, long iVal) {
        return newCounter(Interns.info(name, desc), iVal);
    }

    /**
     * 创建一个可变long计数器
     * @param info
     * @param iVal
     * @return
     */
    public synchronized MutableCounterLong newCounter(MetricsInfo info, long iVal) {
        checkMetricName(info.name());
        MutableCounterLong ret = new MutableCounterLong(info, iVal);
        metricsMap.put(info.name(), ret);
        return ret;
    }

    /**
     * 创建一个可变int估值
     * @param name
     * @param desc
     * @param iVal
     * @return
     */
    public MutableGaugeInt newGauge(String name, String desc, int iVal) {
        return newGauge(Interns.info(name, desc), iVal);
    }

    /**
     * 创建一个可变int估值
     * @param info
     * @param iVal
     * @return
     */
    public synchronized MutableGaugeInt newGauge(MetricsInfo info, int iVal) {
        checkMetricName(info.name());
        MutableGaugeInt ret = new MutableGaugeInt(info, iVal);
        metricsMap.put(info.name(), ret);
        return ret;
    }

    /**
     * 创建一个可变long估值
     * @param name
     * @param desc
     * @param iVal
     * @return
     */
    public MutableGaugeLong newGauge(String name, String desc, long iVal) {
        return newGauge(Interns.info(name, desc), iVal);
    }

    /**
     * 创建一个可变long估值
     * @param info
     * @param iVal
     * @return
     */
    public synchronized MutableGaugeLong newGauge(MetricsInfo info, long iVal) {
        checkMetricName(info.name());
        MutableGaugeLong ret = new MutableGaugeLong(info, iVal);
        metricsMap.put(info.name(), ret);
        return ret;
    }

    /**
     * 创建一个可变float估值
     * @param name
     * @param desc
     * @param iVal
     * @return
     */
    public MutableGaugeFloat newGauge(String name, String desc, float iVal) {
        return newGauge(Interns.info(name, desc), iVal);
    }

    /**
     * 创建一个可变float估值
     * @param info
     * @param iVal
     * @return
     */
    public synchronized MutableGaugeFloat newGauge(MetricsInfo info, float iVal) {
        checkMetricName(info.name());
        MutableGaugeFloat ret = new MutableGaugeFloat(info, iVal);
        metricsMap.put(info.name(), ret);
        return ret;
    }

    public synchronized MutableQuantiles newQuantiles(String name, String desc,
                                                      String sampleName, String valueName, int interval) {

    }

}
