package org.lixl.hadoop.lixlsource.io;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.conf.Configurable;
import org.lixl.hadoop.lixlsource.conf.Configuration;
import org.lixl.hadoop.lixlsource.util.ReflectionUtils;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个比较器 用于 WritableComparable
 * Created by lxl on 20/1/20.
 *
 * 提供了对原始 compare()方法对一个默认实现，该方法能反序列化将在流中进行比较的对象，并调用对象的compare()方法。
 * 充当RawComparator实例工厂（已注册Writable的实现）
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public class WritableComparator implements RawComparator, Configurable {

    private static final ConcurrentHashMap<Class, WritableComparator> comparators = new ConcurrentHashMap<>();  //registry
    private Configuration conf;

    public static WritableComparator get(Class<? extends WritableComparable> c) {
        return get(c, null);
    }

    public static WritableComparator get(Class<? extends WritableComparable> c, Configuration conf) {
        WritableComparator comparator = comparators.get(c);
        if(comparator == null) {
            forceInit(c);
            comparator = comparators.get(c);
            if(comparator == null) {
                comparator = new WritableComparator(c, conf, true);
            }
        }
        ReflectionUtils.setConf(comparator, conf);
        return comparator;
    }

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }

    private static void forceInit(Class<?> cls) {
        try {
            Class.forName(cls.getName(), true, cls.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("不能初始化类 " + cls, e);
        }
    }

    /**
     * 注册一个优化的比较器用作WritableComparable的实现
     * @param c
     * @param comparator
     */
    public static void define(Class c, WritableComparator comparator) {
        comparators.put(c, comparator);
    }

    private final Class<? extends WritableComparable> keyClass;
    private final WritableComparable key1;
    private final WritableComparable key2;
    private final DataInputBuffer buffer;

    protected WritableComparator() {
        this(null);
    }
    protected WritableComparator(Class<? extends WritableComparable> keyClass) {
        this(keyClass, null, false);
    }
    protected WritableComparator(Class<? extends WritableComparable> keyClass, boolean createInstances) {
        this(keyClass, null, createInstances);
    }
    protected WritableComparator(Class<? extends WritableComparable> keyClass, Configuration conf, boolean createInstances) {
        this.keyClass = keyClass;
        this.conf = (conf != null) ? conf : new Configuration();
        if(createInstances) {
            key1 = newKey();
            key2 = newKey();
            buffer = new DataInputBuffer();
        } else {
            key1 = key2 = null;
            buffer = null;
        }
    }

    public Class<? extends WritableComparable> getKeyClass() {
        return keyClass;
    }

    public WritableComparable newKey() {
        //return ReflectionUtils.newInstance(keyClass, conf);
        return null;
    }

    /**
     * 最佳钩子。重写来序列化文件排序
     * @param b1
     * @param s1
     * @param l1
     * @param b2
     * @param s2
     * @param l2
     * @return
     */
    @Override
    public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
        try {
            buffer.reset(b1, s1, l1);
            key1.readFields(buffer);
            buffer.reset(b2, s2, l2);
            key2.readFields(buffer);
            buffer.reset(null, 0, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return compare(key1, key2);
    }

    public int compare(WritableComparable a, WritableComparable b) {
        return a.compareTo(b);
    }

    @Override
    public int compare(Object a, Object b) {
        return compare((WritableComparable)a, (WritableComparable)b);
    }

    public int compareBytes(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
        //return FastByteComparisons.compareTo(b1, s1, l1, b2, s2, l2);
        return 0;
    }

}
