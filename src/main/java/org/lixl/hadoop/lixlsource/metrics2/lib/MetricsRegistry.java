package org.lixl.hadoop.lixlsource.metrics2.lib;

import com.google.common.collect.Maps;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsException;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsRecordBuilder;
import org.lixl.hadoop.lixlsource.metrics2.MetricsTag;
import org.lixl.hadoop.lixlsource.metrics2.impl.MsInfo;

import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 一个可选的指标注册类用于创建和维护一个可变指标集合，使写指标源变得简单。
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

    /**
     * 创建一个可变的标准 估算一个值的流的分位数
     * @param name
     * @param desc
     * @param sampleName
     * @param valueName
     * @param interval
     * @return
     */
    public synchronized MutableQuantiles newQuantiles(String name, String desc,
                                                      String sampleName, String valueName, int interval) {
        checkMetricName(name);
        if(interval <= 0) {
            throw new MetricsException("间隔应该为正数. 传入的值是: " + interval);
        }
        MutableQuantiles ret = new MutableQuantiles(name, desc, sampleName, valueName, interval);
        metricsMap.put(name, ret);
        return ret;
    }

    public synchronized MutableStat newStat(String name, String desc,
                                            String sampleName, String valueName, boolean extended) {
        checkMetricName(name);
        MutableStat ret = new MutableStat(name, desc, sampleName, valueName, extended);
        metricsMap.put(name, ret);
        return ret;
    }

    public MutableStat newStat(String name, String desc, String sampleName, String valueName) {
        return newStat(name, desc, sampleName, valueName, false);
    }

    public MutableRate newRate(String name) {
        return newRate(name, name, false);
    }

    public MutableRate newRate(String name, String description) {
        return newRate(name, description, false);
    }

    public MutableRate newRate(String name, String desc, boolean extended) {
        return newRate(name, desc, extended, true);
    }

    @InterfaceAudience.Private
    public synchronized MutableRate newRate(String name, String desc, boolean extended, boolean returnExisting) {
        if(returnExisting) {
            MutableMetric rate = metricsMap.get(name);
            if(rate != null) {
                if(rate instanceof MutableRate) {
                    return (MutableRate) rate;
                }
                throw new MetricsException("非metrics类型 " + rate.getClass() + " for " + name);
            }
        }
        checkMetricName(name);
        MutableRate ret = new MutableRate(name, desc, extended);
        metricsMap.put(name, ret);
        return ret;
    }

    public synchronized MutableRatesWithAggregation newRatesWithAggregation(String name) {
        checkMetricName(name);
        MutableRatesWithAggregation rates = new MutableRatesWithAggregation();
        metricsMap.put(name, rates);
        return rates;
    }

    public synchronized MutableRollingAverages newMutableRollingAverages(String name, String valueName) {
        checkMetricName(name);
        MutableRollingAverages rollingAverages = new MutableRollingAverages(valueName);
        metricsMap.put(name, rollingAverages);
        return rollingAverages;
    }

    synchronized void add(String name, MutableMetric metric) {
        checkMetricName(name);
        metricsMap.put(name, metric);
    }

    public synchronized void add(String name, long value) {
        MutableMetric m = metricsMap.get(name);

        if(m != null) {
            if(m instanceof MutableStat) {
                ((MutableStat) m).add(value);
            }else {
                throw new MetricsException("Unsupported add(value) for metric " + name);
            }
        }else {
            metricsMap.put(name, newRate(name));    //默认是一个比率标准
            add(name, value);
        }
    }

    public MetricsRegistry setContext(String name) {
        return tag(MsInfo.Context, name, true);
    }

    public MetricsRegistry tag(String name, String description, String value) {
        return tag(name, description, value, false);
    }

    public MetricsRegistry tag(String name, String description, String value, boolean override) {
        return tag(Interns.info(name, description), value, override);
    }

    public synchronized MetricsRegistry tag(MetricsInfo info, String value, boolean override) {
        if(!override) {
            checkTagName(info.name());
        }
        tagsMap.put(info.name(), Interns.tag(info, value));
        return this;
    }

    public MetricsRegistry tag(MetricsInfo info, String value) {
        return tag(info, value, false);
    }

    Collection<MetricsTag> tags() {
        return tagsMap.values();
    }

    Collection<MutableMetric> metrics() {
        return metricsMap.values();
    }


    private void checkMetricName(String name) {
        boolean foundWhitespace = false;
        for(int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if(Character.isWhitespace(c)) {
                foundWhitespace = true;
                break;
            }
        }
        if(foundWhitespace) {
            throw new MetricsException("指标名 '" + name + "' 包含非法的空白字符");
        }
        if(metricsMap.containsKey(name)) {
            throw new MetricsException("指标名 " + name + "已存在！");
        }
    }

    public void checkTagName(String name) {
        if(tagsMap.containsKey(name)) {
            throw new MetricsException("标签" + name + "已存在！");
        }
    }

    public synchronized void snapshot(MetricsRecordBuilder builder, boolean all) {
        for(MetricsTag tag : tags()) {
            builder.add(tag);
        }
        for(MutableMetric metric : metrics()) {
            metric.snapshot(builder, all);
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "{", "}")
                .add("info=" + metricsInfo.toString())
                .add("tags=" + tags())
                .add("metrics=" + metrics())
                .toString();

    }

}
