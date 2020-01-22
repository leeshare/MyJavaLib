package org.lixl.hadoop.lixlsource.metrics2;

import com.google.common.base.Objects;

import java.util.StringJoiner;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 不可变指标
 * Created by lxl on 20/1/21.
 */
public abstract class AbstractMetric implements MetricsInfo {
    private final MetricsInfo info;

    protected AbstractMetric(MetricsInfo info) {
        this.info = checkNotNull(info, "metric info");
    }

    public String name() {
        return info.name();
    }

    public String description() {
        return info.description();
    }

    protected MetricsInfo info() {
        return info;
    }

    /**
     * 获取指标的值
     * @return
     */
    public abstract Number value();

    /**
     * 获取指标类型
     * @return
     */
    public abstract MetricType type();
    /**
     * 接受一个访问者接口
     * @param visitor
     */
    public abstract void visit(MetricsVisitor visitor);

    public boolean equals(Object obj) {
        if(obj instanceof AbstractMetric) {
            final AbstractMetric other = (AbstractMetric) obj;
            return Objects.equal(info, other.info()) && Objects.equal(value(), other.value());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(info, value());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "{", "}")
                .add("info=" + info)
                .add("value=" + value())
                .toString();
    }
}
