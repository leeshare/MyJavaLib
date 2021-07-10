package org.lixl.hadoop.lixlsource.metrics2;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

import com.google.common.base.Objects;

import java.util.StringJoiner;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 不可变标签用于指标
 * Created by lxl on 20/1/21.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MetricsTag implements MetricsInfo {
    private final MetricsInfo info;
    private final String value;

    public MetricsTag(MetricsInfo info, String value) {
        this.info = checkNotNull(info, "tag info");
        this.value = value;
    }

    @Override
    public String name() {
        return info.name();
    }

    @Override
    public String description() {
        return info.description();
    }

    public MetricsInfo info() {
        return info;
    }

    public String value() {
        return value;
    }

    public boolean equals(Object obj) {
        //instanceof 用于判断 某对象 是否是某个超类或接口的子类或实现类
        if (obj instanceof MetricsTag) {
            final MetricsTag other = (MetricsTag) obj;
            return Objects.equal(info, other.info()) && Objects.equal(value, other.value());

        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(info, value);
    }

    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "{", "}")
                .add("info=" + info)
                .add("value=" + value())
                .toString();
    }
}
