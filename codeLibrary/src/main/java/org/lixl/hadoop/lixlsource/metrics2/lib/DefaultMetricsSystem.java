package org.lixl.hadoop.lixlsource.metrics2.lib;

import com.google.common.annotations.VisibleForTesting;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsException;
import org.lixl.hadoop.lixlsource.metrics2.MetricsSystem;
import org.lixl.hadoop.lixlsource.metrics2.impl.MetricsSystemImpl;

import javax.management.ObjectName;
import java.util.concurrent.atomic.AtomicReference;

@InterfaceAudience.Public
@InterfaceStability.Evolving
public enum DefaultMetricsSystem {
    INSTANCE;

    private AtomicReference<MetricsSystem> impl = new AtomicReference<>(new MetricsSystemImpl());
    @VisibleForTesting
    volatile boolean miniClusterMode = false;

    transient final UniqueNames mBeanNames = new UniqueNames();
    transient final UniqueNames sourceNames = new UniqueNames();

    public static MetricsSystem initialize(String prefix) {
        return INSTANCE.init(prefix);
    }

    MetricsSystem init(String prefix) {
        return impl.get().init(prefix);
    }

    public static MetricsSystem instance() {
        return INSTANCE.getImpl();
    }

    public static void shutdown() {
        INSTANCE.shutdownInstance();
    }

    void shutdownInstance() {
        boolean last = impl.get().shutdown();
        if (last) {
            synchronized (this) {
                mBeanNames.map.clear();
                sourceNames.map.clear();
            }
        }
    }

    @InterfaceAudience.Private
    public static MetricsSystem setInstance(MetricsSystem ms) {
        return INSTANCE.setImpl(ms);
    }

    MetricsSystem setImpl(MetricsSystem ms) {
        return impl.getAndSet(ms);
    }

    MetricsSystem getImpl() {
        return impl.get();
    }

    @VisibleForTesting
    public static void setMiniClusterMode(boolean choice) {
        INSTANCE.miniClusterMode = choice;
    }

    @VisibleForTesting
    public static boolean inMiniClusterMode() {
        return INSTANCE.miniClusterMode;
    }

    @InterfaceAudience.Private
    public static ObjectName newMBeanName(String name) {
        return INSTANCE.newObjectName(name);
    }

    @InterfaceAudience.Private
    public static void removeMBeanName(ObjectName name) {
        INSTANCE.removeObjectName(name.toString());
    }

    @InterfaceAudience.Private
    public static void removeSourceName(String name) {
        INSTANCE.removeSource(name);
    }

    @InterfaceAudience.Private
    public static String sourceName(String name, boolean dupOK) {
        return INSTANCE.newSourceName(name, dupOK);
    }

    synchronized ObjectName newObjectName(String name) {
        try {
            if (mBeanNames.map.containsKey(name) && !miniClusterMode) {
                throw new MetricsException(name + " 已存在！");
            }
            return new ObjectName(mBeanNames.uniqueName(name));
        } catch (Exception e) {
            throw new MetricsException(e);
        }
    }

    synchronized void removeObjectName(String name) {
        mBeanNames.map.remove(name);
    }

    synchronized void removeSource(String name) {
        sourceNames.map.remove(name);
    }

    synchronized String newSourceName(String name, boolean dupOK) {
        if (sourceNames.map.containsKey(name)) {
            if (dupOK) {
                return name;
            } else if (!miniClusterMode) {
                throw new MetricsException("指标源 " + name + " 已存在！");
            }
        }
        return sourceNames.uniqueName(name);
    }


}
