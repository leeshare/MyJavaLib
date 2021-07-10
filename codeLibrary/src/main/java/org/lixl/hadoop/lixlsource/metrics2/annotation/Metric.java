package org.lixl.hadoop.lixlsource.metrics2.annotation;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

import java.lang.annotation.*;

/**
 * 一个单独指标用于注解一个字段或方法的 注解接口
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Metric {
    public enum Type {
        DEFAULT, COUNTER, GAUGE, TAG
    }

    /**
     * 速记 于 可选名和描述
     *
     * @return
     */
    String[] value() default {};

    String about() default "";

    String sampleName() default "0ps";

    String valueName() default "Time";

    boolean always() default false;

    Type type() default Type.DEFAULT;

}
