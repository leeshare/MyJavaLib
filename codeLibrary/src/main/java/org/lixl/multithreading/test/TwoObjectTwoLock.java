package org.lixl.multithreading.test;

/**
 * 两个线程 分别访问同一个类的两个不同实例 的相同名称的同步方法
 * 效果却是以异步的方式运行。
 * 虽然使用了synchronized，但打印方式却是交叉的。
 * 原因是多个对象则创建了多把锁，反而起不到锁的作用。
 * 当改成只有一个对象时，则只创建一把锁，就会顺序打印了。
 */
public class TwoObjectTwoLock {
    private int num = 0;

    synchronized public void addI(String userName) {
        try {
            if (userName.equals("a")) {
                num = 100;
                System.out.println("a set over!");
                Thread.sleep(2000);
            } else {
                num = 200;
                System.out.println("b set over!");
            }
            System.out.println(userName + " num=" + num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TwoObjectTwoLock t1 = new TwoObjectTwoLock();
        //TwoObjectTwoLock t2 = new TwoObjectTwoLock();

        Thread a = new Thread() {
            @Override
            public void run() {
                t1.addI("a");
            }
        };
        a.start();
        Thread b = new Thread() {
            @Override
            public void run() {
                t1.addI("b");
            }
        };
        b.start();
    }
}
