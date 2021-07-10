package org.lixl.hadoop.lixlsource.classification;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 此“注释”用于通知那些使用了方法、类、包的预期的听众的用户
 * Created by Administrator on 1/15/2020.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class InterfaceAudience {

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Public {
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LimitedPrivate {
        String[] value();
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Private {
    }

    private InterfaceAudience() {

    }

}
