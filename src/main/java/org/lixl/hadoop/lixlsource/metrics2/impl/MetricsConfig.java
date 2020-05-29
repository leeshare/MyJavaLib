package org.lixl.hadoop.lixlsource.metrics2.impl;

import com.google.common.base.Splitter;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.SubsetConfiguration;
import org.apache.hadoop.util.StringUtils;
import org.lixl.hadoop.lixlsource.metrics2.MetricsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * 指标配置 用于指标系统实现
 */
class MetricsConfig extends SubsetConfiguration {
    static final Logger LOG = LoggerFactory.getLogger(MetricsConfig.class);

    static final String DEFAULT_FILE_NAME = "hadoop-metrics2.properties";
    static final String PREFIX_DEFAULT = "*.";

    static final String PERIOD_KEY = "period";
    static final int PERIOD_DEFAULT = 10;   //秒

    static final String PERIOD_MILLIS_KEY = "periodMillis";

    static final String QUEUE_CAPACITY_KEY = "queue.capacity";
    static final int QUEUE_CAPACITY_DEFAULT = 1;

    static final String RETRY_DELAY_KEY = "retry.delay";
    static final int RETRY_DEALY_DEFAULT = 10;  //秒
    static final String RETRY_BACKOFF_KEY = "retry.backoff";
    static final int RETRY_BACKOFF_DEAFULT = 2; // 后退因素
    static final String RETRY_COUNT_KEY = "retry.count";
    static final int RETRY_COUNT_DEFAULT = 1;

    static final String JMX_CACHE_TTL_KEY = "jmx.cache.ttl";
    static final String START_MBEANS_KEY = "source.start_mbeans";
    static final String PLUGIN_URLS_KEY = "plugin.urls";

    static final String CONTEXT_KEY = "context";
    static final String NAME_KEY = "name";
    static final String DESC_KEY = "description";
    static final String SOURCE_KEY = "source";
    static final String SINK_KEY = "sink";
    static final String METRIC_FILTER_KEY = "metric.filter";
    static final String RECORD_FILTER_KEY = "record.filter";
    static final String SOURCE_FILTER_KEY = "source.filter";

    static final Pattern INSTANCE_REGEX = Pattern.compile("([^.*]+)\\..+");
    static final Splitter SPLITTER = Splitter.on(',').trimResults();
    private ClassLoader pluginLoader;

    MetricsConfig(Configuration c, String prefix) {
        super(c, StringUtils.toLowerCase(prefix), ".");
    }

    static MetricsConfig create(String prefix) {
        return loadFirst(prefix, "hadoop-metrics2-" + StringUtils.toLowerCase(prefix) + ".properties", DEFAULT_FILE_NAME);
    }

    static MetricsConfig create(String prefix, String... fileNames) {
        return loadFirst(prefix, fileNames);
    }

    static MetricsConfig loadFirst(String prefix, String... fileNames) {
        //...
        return null;
    }

    MetricsFilter getFilter(String prefix) {

        return null;
    }

}