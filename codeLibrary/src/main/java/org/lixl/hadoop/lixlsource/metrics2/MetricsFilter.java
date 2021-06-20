package org.lixl.hadoop.lixlsource.metrics2;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

/**
 * 指标过滤接口。此对象既能用于从指标源过滤指标又能从指标度量输出源过滤指标
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public abstract class MetricsFilter implements MetricsPlugin {
    /**
     * 是否接受这个name
     * @param name
     * @return
     */
    public abstract boolean accepts(String name);
    /**
     * 是否接受这个标签
     * @param tag
     * @return
     */
    public abstract boolean accepts(MetricsTag tag);

    /**
     * 是否接受这些标签
     * @param tags
     * @return
     */
    public abstract boolean accepts(Iterable<MetricsTag> tags);
    public boolean accepts(MetricsRecord record) {
        return accepts(record.name()) && accepts(record.tags());
    }
}
