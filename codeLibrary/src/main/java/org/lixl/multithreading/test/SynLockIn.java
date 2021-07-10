package org.lixl.multithreading.test;

/**
 * synchronized锁重入
 * 在对象锁不释放的情况下可以再次获得对象锁
 */
public class SynLockIn {
    synchronized public void service1() {
        System.out.println("service1");
        service2();
    }

    synchronized public void service2() {
        System.out.println("service2");
        service3();
    }

    synchronized public void service3() {
        System.out.println("service3");
    }

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                SynLockIn o = new SynLockIn();
                o.service1();
            }
        }.start();
    }
}
