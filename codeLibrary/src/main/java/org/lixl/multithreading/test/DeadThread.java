package org.lixl.multithreading.test;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

/**
 * 死锁与synchronized嵌不嵌套无关。因为不使用嵌套的synchronized的代码也会出现死锁。
 */
public class DeadThread implements Runnable {
    public String userName;
    public Object lock1 = new Object();
    public Object lock2 = new Object();

    public void setFlag(String userName) {
        this.userName = userName;
    }

    @Override
    public void run() {
        if (userName.equals("a")) {
            synchronized (lock1) {
                try {
                    System.out.println("username=" + userName);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println("按lock1->lock2代码顺序执行了");
                }
            }
        }
        if (userName.equals("b")) {
            synchronized (lock2) {
                try {
                    System.out.println("username=" + userName);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    System.out.println("按lock2->lock1代码顺序执行了");
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DeadThread t1 = new DeadThread();
        DeadThread t2 = new DeadThread();
        t1.setFlag("a");
        Thread thread1 = new Thread(t1);
        thread1.start();
        Thread.sleep(100);
        t1.setFlag("b");
        Thread thread2 = new Thread(t1);
        thread2.start();
    }
}
