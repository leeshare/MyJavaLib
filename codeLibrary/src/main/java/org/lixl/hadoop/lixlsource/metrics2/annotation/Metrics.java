package org.lixl.hadoop.lixlsource.metrics2.annotation;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

import java.lang.annotation.*;

/**
 * 一组指标的 注解接口
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Metrics {
    /**
     * @return 此指标的名
     */
    String name() default "";

    /**
     * @return 指标的可选描述
     */
    String about() default "";
    /**
     * @return 一组指标的内容名
     */
    String context();
}
