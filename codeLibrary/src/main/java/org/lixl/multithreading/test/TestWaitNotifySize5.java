package org.lixl.multithreading.test;

import java.util.ArrayList;
import java.util.List;

/**
 * notify()虽然执行，但必须等 notify所在的同步块全部执行完，
 * 才能开始执行 wait()之后的
 */
public class TestWaitNotifySize5 {
    private static List list = new ArrayList();

    public static void add() {
        list.add("anyString");
    }

    public static int size() {
        return list.size();
    }

    private static Object lock;

    public static void main(String[] args) throws InterruptedException {
        lock = new Object();
        Thread ta = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (lock) {
                        if (TestWaitNotifySize5.size() != 5) {
                            System.out.println("wait begin " + System.currentTimeMillis());
                            lock.wait();
                            System.out.println("wait end " + System.currentTimeMillis());
                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        ta.start();
        Thread.sleep(50);
        Thread tb = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (lock) {
                        for (int i = 0; i < 10; i++) {
                            TestWaitNotifySize5.add();
                            if (TestWaitNotifySize5.size() == 5) {
                                lock.notify();
                                System.out.println("已发出通知");
                            }
                            System.out.println("添加了" + (i + 1) + "个元素");
                            Thread.sleep(1000);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        tb.start();
    }
}
