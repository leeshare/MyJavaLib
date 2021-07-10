package org.lixl.multithreading.test;

/**
 * volatile 单独用，仅仅是可见性
 * 如果使用 synchronized 也就没有必要使用 volatile了
 * <p>
 * 这里最关键的问题是 i++ 不是原子操作，是非线程安全操作
 */
public class VolatileNoAtom extends Thread {
    volatile public static int count;

    //public static int count;
    private static void addCount()
    //synchronized private static void addCount()
    {
        for (int i = 0; i < 100; i++) {
            count++;
        }
        System.out.println("count=" + count);
    }

    @Override
    public void run() {
        addCount();
    }

    public static void main(String[] args) {
        VolatileNoAtom[] t = new VolatileNoAtom[100];
        for (int i = 0; i < 100; i++) {
            t[i] = new VolatileNoAtom();
        }
        for (int i = 0; i < 100; i++) {
            t[i].start();
        }
    }
}
