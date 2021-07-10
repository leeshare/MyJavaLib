package org.lixl.hadoop.lixlsource.metrics2.impl;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import org.lixl.hadoop.lixlsource.metrics2.MetricsRecord;

import java.util.StringJoiner;

abstract class AbstractMetricsRecord implements MetricsRecord {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MetricsRecord) {
            final MetricsRecord other = (MetricsRecord) obj;
            return Objects.equal(timestamp(), other.timestamp()) &&
                    Objects.equal(name(), other.name()) &&
                    Objects.equal(description(), other.description()) &&
                    Objects.equal(tags(), other.tags()) &&
                    //Objects.equal(metrics(), other.metrics())
                    Iterables.elementsEqual(metrics(), other.metrics());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name(), description(), tags());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "{", "}")
                .add("timestamp=" + timestamp())
                .add("name=" + name())
                .add("description=" + description())
                .add("tags=" + tags())
                .add("metrics=" + Iterables.toString(metrics()))
                .toString();
    }
}
