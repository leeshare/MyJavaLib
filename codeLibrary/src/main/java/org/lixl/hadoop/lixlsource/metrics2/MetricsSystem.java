package org.lixl.hadoop.lixlsource.metrics2;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

/**
 * 指标系统 接口
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public abstract class MetricsSystem implements MetricsSystemMXBean {
    @InterfaceAudience.Private
    public abstract MetricsSystem init(String prefix);

    /**
     * 注册一个指标源
     * @param name
     * @param desc
     * @param source
     * @param <T>
     * @return
     */
    public abstract <T> T register(String name, String desc, T source);

    /**
     * 注销一个指标源
     * @param name
     */
    public abstract void unregisterSource(String name);

    public <T> T register(T source) {
        return register(null, null, source);
    }

    @InterfaceAudience.Private
    public abstract MetricsSource getSource(String name);

    public abstract <T extends MetricsSink> T register(String name, String desc, T sink);

    public abstract void register(Callback callback);

    public abstract void publishMetricsNow();

    public abstract boolean shutdown();


    /**
     * 指标系统回调接口
     */
    public interface Callback {
        /**
         * 在start()之前调用
         */
        void preStart();

        /**
         * 在start()之后调用
         */
        void postStart();

        /**
         * 在stop()之前调用
         */
        void preStop();

        /**
         * 在stop()之后调用
         */
        void postStop();
    }

    /**
     * 方便的抽象类 实现了callback接口
     */
    public static abstract class AbstractCallback implements Callback {
        @Override
        public void preStart() {}
        @Override
        public void postStart() {}
        @Override
        public void preStop() {}
        @Override
        public void postStop() {}
    }
}
