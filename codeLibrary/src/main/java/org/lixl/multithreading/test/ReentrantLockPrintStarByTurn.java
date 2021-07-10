package org.lixl.multithreading.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一对一交替打印
 */
public class ReentrantLockPrintStarByTurn {

    public static class MyPrint {
        Lock lock = new ReentrantLock();
        Condition con = lock.newCondition();
        volatile public boolean isA = true;

        public void printA() {
            try {
                lock.lock();
                while (!isA) {
                    con.await();
                }
                System.out.println("★");
                isA = false;
                Thread.sleep(500);
                con.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void printB() {
            try {
                lock.lock();
                while (isA) {
                    con.await();
                }
                System.out.println("☆");
                isA = true;
                Thread.sleep(500);
                con.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyPrint service = new MyPrint();
        Thread a = new Thread() {
            @Override
            public void run() {
                while (true) {
                    service.printA();
                }
            }
        };
        Thread b = new Thread() {
            @Override
            public void run() {
                while (true) {
                    service.printB();
                }
            }
        };
        a.start();
        b.start();

    }
}
