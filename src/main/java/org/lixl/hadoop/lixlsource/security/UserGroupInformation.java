package org.lixl.hadoop.lixlsource.security;

import com.google.common.annotations.VisibleForTesting;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lxl on 20/1/21.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class UserGroupInformation {
    @VisibleForTesting
    static final Logger LOG = LoggerFactory.getLogger(UserGroupInformation.class);

    /**
     * 在我们更新前, 可用的 ticket window 的百分比
     */
    private static final float TICKET_RENEW_WINDOW = 0.80f;
    private static boolean shouldRenewImmediatelyForTests = false;
    static final String HADOOP_USER_NAME = "HADOOP_USER_NAME";
    static final String HADOOP_PROXY_USER = "HADOOP_PROXY_USER";

    /**
     * 为了单元测试的目的,我们想要从keytab测试登录并且不想等待,直到更新window
     * @param immediate
     */
    @VisibleForTesting
    public static void setShouldRenewImmediatelyForTests(boolean immediate) {
        shouldRenewImmediatelyForTests = immediate;
    }

    static class UgiMetrics {
        final MetricsRegistry
    }

}
