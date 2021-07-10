package org.lixl.multithreading.test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class CustomLock {
    volatile int status = 0;

    private static Unsafe unsafe = null;
    private static long stateOffset;

    //获取unsafe对象
    static {
        Field singleoneInstanceField = null;
        try {
            singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            singleoneInstanceField.setAccessible(true);
            unsafe = (Unsafe) singleoneInstanceField.get(null);
            stateOffset = unsafe.objectFieldOffset(CustomLock.class.getDeclaredField("status"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 实现原子方法
     * <p>
     * 拿到内存对应的指针的偏移量
     *
     * @param oldVal
     * @param newVal
     * @return
     */
    boolean compareAndSet(int oldVal, int newVal) {
        return unsafe.compareAndSwapInt(this, stateOffset, 0, 1);
    }
}
