package org.lixl.multithreading.test;

/**
 * yield()释放cpu资源，但放弃时间不确定
 */
public class TestYield extends Thread {
    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < 500000; i++) {
            Thread.yield();
            count = count + (i + 1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("用时：" + (endTime - beginTime) + "毫秒！  count=" + count);
    }

    public static void main(String[] args) {
        TestYield t = new TestYield();
        t.start();

    }
}
