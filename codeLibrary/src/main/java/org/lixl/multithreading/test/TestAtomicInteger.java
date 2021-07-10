package org.lixl.multithreading.test;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 方法是原子的，但方法和方法之间的调用却不是原子的
 */
public class TestAtomicInteger {
    public static AtomicLong aiRef = new AtomicLong();

    //synchronized
    public void addNum() {
        //System.out.println(aiRef.incrementAndGet());

        System.out.println(Thread.currentThread().getName() + " 加了100之后的值hi：" + aiRef.addAndGet(100));
        aiRef.addAndGet(1);
    }

    public static void main(String[] args) throws InterruptedException {
        TestAtomicInteger countService = new TestAtomicInteger();
        for (int i = 0; i < 5; i++) {
            Thread t1 = new Thread() {
                @Override
                public void run() {
                    countService.addNum();
                }
            };
            t1.start();
        }
        Thread.sleep(1000);
        System.out.println(TestAtomicInteger.aiRef.get());

    }
}

