package org.lixl.hadoop.lixlsource.metrics2.impl;

import com.google.common.collect.Maps;
import org.lixl.hadoop.lixlsource.metrics2.MetricsFilter;
import org.lixl.hadoop.lixlsource.metrics2.MetricsSource;
import org.lixl.hadoop.lixlsource.metrics2.MetricsTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.lixl.hadoop.lixlsource.metrics2.util.Contracts.checkArg;

import static org.lixl.hadoop.lixlsource.metrics2.impl.MetricsConfig.*;

/**
 * 一个适配器类 用于指标源和关联过滤器和jmx实现
 */
class MetricsSourceAdapter implements DynamicMBean {
    private static final Logger LOG = LoggerFactory.getLogger(MetricsSourceAdapter.class);

    private final String prefix, name;
    private final MetricsSource source;
    private final MetricsFilter recordFilter, metricFilter;
    private final HashMap<String, Attribute> attrCache;
    private final MBeanInfoBuilder infoBuilder;
    private final Iterable<MetricsTag> injectedTags;

    private boolean lastRecsCleared;
    private long jmxCacheTS = 0;
    private long jmxCacheTTL;
    private MBeanInfo infoCache;
    private ObjectName mbeanName;
    private final boolean startMBeans;

    static final String METRIC_FILTER_KEY = "metric.filter";
    static final String RECORD_FILTER_KEY = "record.filter";
    static final String SOURCE_FILTER_KEY = "source.filter";


    MetricsSourceAdapter(String prefix, String name, String description,
                         MetricsSource source, Iterable<MetricsTag> injectedTags,
                         MetricsFilter recordFilter, MetricsFilter metricFilter,
                         long jmxCacheTTL, boolean startMBeans) {
        this.prefix = checkNotNull(prefix, "prefix");
        this.name = checkNotNull(name, "name");
        this.source = checkNotNull(source, "source");
        attrCache = Maps.newHashMap();
        infoBuilder = new MBeanInfoBuilder(name, description);
        this.injectedTags = injectedTags;
        this.recordFilter = recordFilter;
        this.metricFilter = metricFilter;
        this.jmxCacheTTL = checkArg(jmxCacheTTL, jmxCacheTTL > 0, "jmxCacheTTL");
        this.startMBeans = startMBeans;
        //初始化 true，这样我们就可以在第一次调用 updateJmxCache 时触发修改 MBeanInfo 缓存
        this.lastRecsCleared = true;
    }

    MetricsSourceAdapter(String prefix, String name, String description,
                         MetricsSource source, Iterable<MetricsTag> injectedTags,
                         long period, MetricsConfig conf) {
        this(prefix, name, description, source, injectedTags,
                conf.getFilter(RECORD_FILTER_KEY),
                conf.getFilter(METRIC_FILTER_KEY),
                period + 1, // hack to avoid most of the "innocuous" races.
                conf.getBoolean(START_MBEANS_KEY, true));
    }

    @Override
    public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
        return null;
    }

    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {

    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        return null;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        return null;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return null;
    }
}
