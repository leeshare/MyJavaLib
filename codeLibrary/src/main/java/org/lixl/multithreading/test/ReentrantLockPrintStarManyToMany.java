package org.lixl.multithreading.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockPrintStarManyToMany {

    public static class MyPrint {
        private ReentrantLock lock;
        private Condition condition;
        private boolean hasValue = false;

        public MyPrint(boolean isFair) {
            lock = new ReentrantLock(isFair);
            condition = lock.newCondition();
        }

        public void set() {
            try {
                lock.lock();
                while (!hasValue) {
                    System.out.println("可能★★");
                    condition.await();
                }
                System.out.println("print ★");
                hasValue = false;
                condition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void get() {
            try {
                lock.lock();
                while (hasValue) {
                    System.out.println("可能☆");
                    condition.await();
                }
                System.out.println("print ☆");
                condition.signalAll();
                hasValue = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        MyPrint service = new MyPrint(true);
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            Thread prod = new Thread() {
                @Override
                public void run() {
                    service.set();
                }
            };
            prod.start();
            Thread consumer = new Thread() {
                @Override
                public void run() {
                    service.get();
                }
            };
            consumer.start();
        }
    }
}
