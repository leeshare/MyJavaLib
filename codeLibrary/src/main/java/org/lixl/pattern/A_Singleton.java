package org.lixl.pattern;

/**
 * 同步 单例模式
 * 延迟加载方式的懒汉模式
 * 提前加载的饥汉模式
 * Created by lxl on 18/7/5.
 */
public class A_Singleton {
    private static A_Singleton instance;
    private final static Object lock = new Object();

    private A_Singleton() {
    }

    public static A_Singleton getInstance() {
        if (instance == null) {
            synchronized (lock) {
                instance = new A_Singleton();
            }
        }
        return instance;
    }

    /**
     * 延迟加载的 懒汉模式
     * DCL 双检查锁机制
     *
     * @return
     */
    public static A_Singleton getInstance2() {
        if (instance == null) {
            synchronized (A_Singleton.class) {
                if (instance == null) {
                    instance = new A_Singleton();
                }
            }
        }
        return instance;
    }


    private static A_Singleton ASingleton = new A_Singleton();

    /**
     * 饥汉模式 提前加载
     *
     * @return
     */
    public static A_Singleton getInstance3() {
        return ASingleton;
    }
}
