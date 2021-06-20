package org.lixl.pattern;

/**
 * 同步 单例模式
 * Created by lxl on 18/7/5.
 */
public class Singleton {
    private static Singleton instance;
    private final static Object lock = new Object();

    private Singleton(){}

    public static Singleton getInstance(){
        if(instance == null){
            synchronized (lock){
                instance = new Singleton();
            }
        }
        return instance;
    }

    public static Singleton getInstance2(){
        if(instance == null){
            synchronized (Singleton.class){
                if(instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
