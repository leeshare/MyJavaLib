package org.lixl.multithreading.test;

import com.google.inject.internal.cglib.proxy.$Callback;

public class ThreadGroupTest {

    public static class ThreadA extends Thread {
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("ThreadName=" + Thread.currentThread().getName());
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ThreadB extends Thread {
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("ThreadName=" + Thread.currentThread().getName());

                    Thread.sleep(1500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ThreadA a = new ThreadA();
        ThreadB b = new ThreadB();
        ThreadGroup group = new ThreadGroup("我的线程组");
        Thread aa = new Thread(group, a);
        Thread bb = new Thread(group, b);
        aa.start();
        bb.start();
        System.out.println("活动的线程数=" + group.activeCount());
        System.out.println("线程组的名称为：" + group.getName());
    }
}
