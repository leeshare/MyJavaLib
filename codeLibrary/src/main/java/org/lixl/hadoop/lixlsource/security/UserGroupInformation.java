package org.lixl.hadoop.lixlsource.security;

import com.google.common.annotations.VisibleForTesting;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.conf.Configuration;
import org.lixl.hadoop.lixlsource.metrics2.annotation.Metric;
import org.lixl.hadoop.lixlsource.metrics2.annotation.Metrics;
import org.lixl.hadoop.lixlsource.metrics2.lib.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Principal;
import java.util.Map;

/**
 * 用于hadoop的用户和用户分组信息
 * Created by lxl on 20/1/21.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class UserGroupInformation {
    //@VisibleForTesting
    //static final Logger LOG = LoggerFactory.getLogger(UserGroupInformation.class);


    /**
     * 在我们更新前, 可用的 ticket window 的百分比
     */
    private static final float TICKET_RENEW_WINDOW = 0.80f;
    private static boolean shouldRenewImmediatelyForTests = false;
    static final String HADOOP_USER_NAME = "HADOOP_USER_NAME";
    static final String HADOOP_PROXY_USER = "HADOOP_PROXY_USER";

    private final Subject subject;
    // All non-static fields must be read-only caches that come from the subject.
    private final User user;

    UserGroupInformation(Subject subject) {
        this.subject = subject;
        // do not access ANY private credentials since they are mutable
        // during a relogin.  no principal locking necessary since
        // relogin/logout does not remove User principal.
        this.user = subject.getPrincipals(User.class).iterator().next();
        if (user == null || user.getName() == null) {
            throw new IllegalStateException("Subject does not contain a valid User");
        }
    }

    /**
     * 为了单元测试的目的,我们想要从keytab测试登录并且不想等待,直到更新window
     * @param immediate
     */
    @VisibleForTesting
    public static void setShouldRenewImmediatelyForTests(boolean immediate) {
        shouldRenewImmediatelyForTests = immediate;
    }

    @Metrics(about="用户和分组相关的指标", context="ugi")
    static class UgiMetrics {
        final MetricsRegistry registry = new MetricsRegistry("UgiMetrics");
        @Metric("按Kerberos协议 登录和等待时长 成功比率(milliseconds)")
        MutableRate loginSuccess;
        @Metric("按Kerberos协议 登录和等待时长 失败比率(milliseconds)")
        MutableRate loginFailure;
        @Metric("获得分组") MutableRate getGroups;
        MutableQuantiles[] getGroupsQuantiles;
        @Metric("从启动 延续失败")
        private MutableGaugeLong renewalFailuresTotal;
        @Metric("从上次成功登录 延续到 失败")
        private MutableGaugeInt renewalFailures;

        static UgiMetrics create() {
            return DefaultMetricsSystem.instance().register(new UgiMetrics());
        }
        static void reattach() {
            //metrics = UgiMetrics.create();
        }
        void addGetGroups(long latency) {
            getGroups.add(latency);
            if(getGroupsQuantiles != null) {
                for(MutableQuantiles q : getGroupsQuantiles) {
                    q.add(latency);
                }
            }
        }

        MutableGaugeInt getRenewalFailures() {
            return renewalFailures;
        }
    }

    @InterfaceAudience.Private
    public static class HadoopLoginModule implements LoginModule {
        private Subject subject;

        @Override
        public boolean abort() throws LoginException {
            return true;
        }

        private <T extends Principal> T getCannonicalUser(Class<T> cls) {
            for(T user: subject.getPrincipals(cls)) {
                return user;
            }
            return null;
        }
        @Override
        public boolean commit() throws LoginException {
            //if(LOG.isDebugEnabled()) {
            //    LOG.debug("hadoop 登录提交");
            //}
            if(!subject.getPrincipals(User.class).isEmpty()) {
                //if(LOG.isDebugEnabled()) {
                //    LOG.debug("使用已存在subject:" + subject.getPrincipals());
                //}
                return true;
            }

            //...

            return false;

        }

        @Override
        public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> shareState, Map<String, ?> options) {
            this.subject = subject;
        }

        @Override
        public boolean login() throws LoginException {
            //if(LOG.isDebugEnabled()) {
            //    LOG.debug("hadoop login");
            //}
            return true;
        }

        @Override
        public boolean logout() throws LoginException {
            //if(LOG.isDebugEnabled()) {
            //    LOG.debug("hadoop logout");
            //}
            return true;
        }


    }


    //static UgiMetrics metrics =


    @InterfaceAudience.Public
    @InterfaceStability.Evolving
    public enum AuthenticationMethod {

    }


    private static Configuration conf;

    public static boolean isInitialized() {
        return conf != null;
    }

    @InterfaceAudience.Public
    @InterfaceStability.Evolving
    public static UserGroupInformation getCurrentUser() throws IOException {
        AccessControlContext context = AccessController.getContext();
        Subject subject = Subject.getSubject(context);
        if (subject == null || subject.getPrincipals(User.class).isEmpty()) {
            //return getLoginUser();
            return null;
        } else {
            return new UserGroupInformation(subject);
        }
    }

}
