package org.lixl.hadoop.lixlsource.metrics2.lib;

import com.google.common.base.Objects;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;

import java.util.StringJoiner;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 创建一个简单点的指标实现信息
 * Created by lxl on 20/1/30.
 */
class MetricsInfoImpl implements MetricsInfo {
    private final String name, description;

    MetricsInfoImpl(String name, String description) {
        this.name = checkNotNull(name, "name");
        this.description = checkNotNull(description, "description");
    }

    @Override
    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MetricsInfo) {
            MetricsInfo other = (MetricsInfo) obj;
            return Objects.equal(name, other.name()) &&
                    Objects.equal(description, other.description());

        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, description);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "{", "}")
                .add("name=" + name)
                .add("description" + description)
                .toString();
    }

}
