package org.lixl.hadoop.lixlsource.metrics2;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

import java.util.Collection;

/**
 * 一个指标的不可变快照 包含时间戳
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public interface MetricsRecord {
    long timestamp();

    String name();

    String description();

    String context();

    Collection<MetricsTag> tags();

    Iterable<AbstractMetric> metrics();
}
