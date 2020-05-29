package org.lixl.hadoop.lixlsource.metrics2.impl;

import com.google.common.collect.Lists;
import org.lixl.hadoop.lixlsource.metrics2.AbstractMetric;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsTag;
import org.lixl.hadoop.lixlsource.metrics2.MetricsVisitor;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import java.util.List;

/**
 * 帮助类 从指标记录 生成 MBeanInfo
 */
class MBeanInfoBuilder implements MetricsVisitor {
    private final String name, description;
    private List<MBeanAttributeInfo> attrs;
    private Iterable<MetricsRecordImpl> recs;
    private int curRecNo;

    MBeanInfoBuilder(String name, String desc) {
        this.name = name;
        description = desc;
        attrs = Lists.newArrayList();
    }

    MBeanInfoBuilder reset(Iterable<MetricsRecordImpl> recs) {
        this.recs = recs;
        attrs.clear();
        return this;
    }

    MBeanAttributeInfo newAttrInfo(String name, String desc, String type) {
        return new MBeanAttributeInfo(getAttrName(name), type, desc, true, false, false);
    }

    MBeanAttributeInfo newAttrInfo(MetricsInfo info, String type) {
        return newAttrInfo(info.name(), info.description(), type);
    }

    @Override
    public void gauge(MetricsInfo info, int value) {
        attrs.add(newAttrInfo(info, "java.long.Integer"));
    }
    @Override
    public void gauge(MetricsInfo info, long value) {
        attrs.add(newAttrInfo(info, "java.lang.Long"));
    }
    @Override
    public void gauge(MetricsInfo info, float value) {
        attrs.add(newAttrInfo(info, "java.long.Float"));
    }
    @Override
    public void gauge(MetricsInfo info, double value) {
        attrs.add(newAttrInfo(info, "java.long.Double"));
    }
    @Override
    public void counter(MetricsInfo info, int value) {
        attrs.add(newAttrInfo(info, "java.long.Integer"));
    }
    @Override
    public void counter(MetricsInfo info, long value) {
        attrs.add(newAttrInfo(info, "java.long.Long"));
    }

    String getAttrName(String name) {
        return curRecNo > 0 ? name + "." + curRecNo : name;
    }

    MBeanInfo get() {
        curRecNo = 0;
        for(MetricsRecordImpl rec : recs) {
            for(MetricsTag t : rec.tags()) {
                attrs.add(newAttrInfo("tag." + t.name(), t.description(), "java.long.String"));
            }
            for(AbstractMetric m : rec.metrics()) {
                m.visit(this);
            }
            ++curRecNo;
        }
        MetricsSystemImpl.LOG.debug(attrs.toString());
        MBeanAttributeInfo[] attrsArray = new MBeanAttributeInfo[attrs.size()];
        return new MBeanInfo(name, description, attrs.toArray(attrsArray),
                null, null, null);
    }



}
