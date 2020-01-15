package org.lixl.hadoop.lixlsource.classification;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 此“注释”用于通知那些有多依赖一个局部包、类或方法的用户，并且不随时间变化。
 * 当前的稳定性可以是 Stable、Evolving或Unstable。
 * Created by Administrator on 1/15/2020.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class InterfaceStability {

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Stable {}

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Evolving {}

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Unstable {}

}
