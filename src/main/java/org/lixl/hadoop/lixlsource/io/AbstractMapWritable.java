package org.lixl.hadoop.lixlsource.io;

import com.google.common.annotations.VisibleForTesting;
import org.apache.hadoop.io.DataOutputBuffer;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.conf.Configurable;
import org.lixl.hadoop.lixlsource.conf.Configuration;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@InterfaceAudience.Public
@InterfaceStability.Stable
public abstract class AbstractMapWritable implements Writable, Configurable {
    private AtomicReference<Configuration> conf;
    @VisibleForTesting
    Map<Class<?>, Byte> classToIdMap = new ConcurrentHashMap<>();
    @VisibleForTesting
    Map<Byte, Class<?>> idToClassMap = new ConcurrentHashMap<>();
    private volatile byte newClasses = 0;
    byte getNewClasses() {
        return newClasses;
    }
    private synchronized void addToMap(Class<?> clazz, byte id) {
        if(classToIdMap.containsKey(clazz)) {
            byte b = classToIdMap.get(clazz);
            if(b != id) {
                throw new IllegalArgumentException("Class " + clazz.getName() + "已注册但映射" + b + "而无" + id);
            }
        }
        if(idToClassMap.containsKey(id)) {
            Class<?> c = idToClassMap.get(id);
            if(!c.equals(clazz)) {
                throw new IllegalArgumentException("Id " + id + "存在但未映射" + c.getName() + "而无" + clazz.getName());
            }
        }
        classToIdMap.put(clazz, id);
        idToClassMap.put(id, clazz);
    }

    protected synchronized void addToMap(Class<?> clazz) {
        if(classToIdMap.containsKey(clazz)){
            return;
        }
        if(newClasses + 1 > Byte.MAX_VALUE) {
            throw new IndexOutOfBoundsException("添加一个额外类将超过允许最大值");
        }
        byte id = ++newClasses;
        addToMap(clazz, id);
    }
    protected Class<?> getClass(byte id) {
        return idToClassMap.get(id);
    }
    protected byte getId(Class<?> clazz) {
        return classToIdMap.containsKey(clazz) ? classToIdMap.get(clazz) : -1;
    }
    protected synchronized void copy(Writable other) {
        if(other != null) {
            try {
                DataOutputBuffer out = new DataOutputBuffer();
                other.write(out);
                DataInputBuffer in = new DataInputBuffer();
                in.reset(out.getData(), out.getLength());
                readFields(in);
            } catch (IOException e) {
                throw new IllegalArgumentException("map不能复制：" + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("源map不能为null");
        }
    }

    protected AbstractMapWritable() {
        this.conf = new AtomicReference<>();
        //addToMap(ArrayWritable.class, (byte)-127);

        //... 2020
    }
    @Override
    public Configuration getConf() {
        return conf.get();
    }
    @Override
    public void setConf(Configuration conf) {
        this.conf.set(conf);
    }
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeByte(newClasses);
        for(byte i = 1; i <= newClasses; i++) {
            out.writeByte(i);
            out.writeUTF(getClass(i).getName());
        }
    }
    @Override
    public void readFields(DataInput in) throws IOException {
        newClasses = in.readByte();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for(int i = 0; i < newClasses; i++) {
            byte id = in.readByte();
            String className = in.readUTF();
            try {
                addToMap(classLoader.loadClass(className), id);
            } catch (ClassNotFoundException e) {
                throw new IOException(e);
            }
        }
    }

}
