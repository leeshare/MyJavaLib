package org.lixl.multithreading.test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    public static class MyService {
        private static Lock lock = new ReentrantLock();

        public void testMethod() {
            lock.lock();
            for (int i = 0; i < 5; i++) {
                System.out.println("ThreadName=" + Thread.currentThread().getName() + " " + (i + 1));
            }
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        MyService service = new MyService();
        for (int i = 0; i < 5; i++) {
            Thread a = new Thread() {
                @Override
                public void run() {
                    service.testMethod();
                }
            };
            a.start();
        }
    }
}
