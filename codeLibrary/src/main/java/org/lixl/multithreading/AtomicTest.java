package org.lixl.multithreading;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lxl on 18/7/5.
 */
public class AtomicTest {

    public static AtomicInteger race = new AtomicInteger(0);

    public static void increase() {
        race.incrementAndGet();
    }

    private static final int THREADS_COUNT = 20;

    public static void main(String[] args) throws Exception {
        System.out.println("begin");
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        increase();
                    }
                }
            });
            threads[i].start();
        }

        while (Thread.activeCount() > 1) {
            Thread.yield();     //线程让步,当前线程由"运行状态"变为"就绪状态"
        }
        System.out.println(race.toString());

        System.out.println("end");
    }
}
