package org.lixl.multithreading.test;

/**
 * suspend 被废除的原因
 * 使用不当导致永久中断
 */
public class SynchronizedObject {
    synchronized public void printString() {
        System.out.println("begin");
        if (Thread.currentThread().getName().equals("a")) {
            System.out.println("a线程永久 suspend了");
            Thread.currentThread().suspend();
        }
        System.out.println("end");
    }

    public static void main(String[] args) {
        try {
            final SynchronizedObject object = new SynchronizedObject();
            Thread t1 = new Thread() {
                @Override
                public void run() {
                    object.printString();
                }
            };
            t1.setName("a");
            t1.start();
            Thread.sleep(1000);

            Thread t2 = new Thread() {
                @Override
                public void run() {
                    System.out.println("thread2启动了，但进入不了 printString方法");
                    System.out.println("因为printString()方法被a线程锁定并永远suspend暂停了");
                    object.printString();
                }
            };
            t2.start();
            //Thread.sleep(3000);
            //t1.resume();
        } catch (InterruptedException e) {

        }
    }
}
