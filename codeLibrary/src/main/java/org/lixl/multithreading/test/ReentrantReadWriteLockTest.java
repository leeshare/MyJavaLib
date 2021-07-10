package org.lixl.multithreading.test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {
    public static class Service {
        private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        public void read() {
            try {
                try {
                    lock.readLock().lock();
                    System.out.println("获得读锁" + Thread.currentThread().getName() + " " + System.currentTimeMillis());
                    Thread.sleep(10000);
                } finally {
                    lock.readLock().unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void write() {
            try {
                try {
                    lock.writeLock().lock();
                    System.out.println("获得写锁" + Thread.currentThread().getName() + " " + System.currentTimeMillis());
                    Thread.sleep(10000);
                } finally {
                    lock.writeLock().unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Service service = new Service();
        Thread a = new Thread() {
            @Override
            public void run() {
                service.read();
                service.write();
            }
        };
        a.start();
        Thread b = new Thread() {
            @Override
            public void run() {
                service.read();
                service.write();
            }
        };
        b.start();
    }
}
