package org.lixl.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SyncTest {

    static List<Thread> list = new ArrayList<Thread>();
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                synchronized (lock) {
                    try {
                        //synchronized 是 先进后出
                        System.out.println("执行顺序 " + Thread.currentThread().getName());
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "t" + i);
            list.add(t);
        }

        System.out.println("启动顺序 和调用顺序 0-9");

        synchronized (lock) {
            for (Thread thread : list) {
                System.out.println("启动顺序 " + thread.getName());
                thread.start();

                //为了让线程 进入队列的顺序是一致的！！！
                TimeUnit.MILLISECONDS.sleep(1);
            }
        }
    }
}
