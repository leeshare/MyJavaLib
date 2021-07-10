package org.lixl.multithreading.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockWithCondition {
    public static class MyService {
        private Lock lock = new ReentrantLock();
        private Condition condition = lock.newCondition();

        public void await() {
            try {
                lock.lock();
                System.out.println("A");
                condition.await();
                System.out.println("B");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("锁释放了");
            }
        }

        public void signal() {
            try {
                lock.lock();
                System.out.println("signal时间为" + System.currentTimeMillis());
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyService service = new MyService();
        Thread t = new Thread() {
            @Override
            public void run() {
                service.await();
            }
        };
        t.start();
        Thread.sleep(3000);
        service.signal();
    }
}
