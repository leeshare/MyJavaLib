package org.lixl.multithreading.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockWithMoreConditions {
    public static class MyService {
        private Lock lock = new ReentrantLock();
        public Condition conditionA = lock.newCondition();
        public Condition conditionB = lock.newCondition();

        public void awaitA() {
            try {
                lock.lock();
                System.out.println("begin awaitA 时间为" + System.currentTimeMillis() + " ThreadName=" + Thread.currentThread().getName());
                conditionA.await();
                System.out.println("  end awaitA 时间为" + System.currentTimeMillis() + " ThreadName=" + Thread.currentThread().getName());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void awaitB() {
            try {
                lock.lock();
                System.out.println("begin awaitB 时间为" + System.currentTimeMillis() + " ThreadName=" + Thread.currentThread().getName());
                conditionB.await();
                System.out.println("  end awaitB 时间为" + System.currentTimeMillis() + " ThreadName=" + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void signalAll_A() {
            try {
                lock.lock();
                System.out.println(" signalAll_A 时间为" + System.currentTimeMillis() + " ThreadName=" + Thread.currentThread().getName());
                conditionA.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public void signalAll_B() {
            try {
                lock.lock();
                System.out.println(" signalAll_B 时间为" + System.currentTimeMillis() + " ThreadName=" + Thread.currentThread().getName());
                conditionB.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        MyService service = new MyService();
        Thread a = new Thread() {
            @Override
            public void run() {
                service.awaitA();
            }
        };
        a.setName("A");
        a.start();
        Thread b = new Thread() {
            @Override
            public void run() {
                service.awaitB();
            }
        };
        b.setName("B");
        b.start();
        Thread.sleep(3000);
        service.signalAll_A();
    }
}
