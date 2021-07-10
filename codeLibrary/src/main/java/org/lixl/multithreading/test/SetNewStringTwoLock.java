package org.lixl.multithreading.test;

/**
 *
 */
public class SetNewStringTwoLock {
    private String lock = "123";

    public void testMethod() {
        try {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + " begin " + System.currentTimeMillis());
                lock = "456";
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + " end " + System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SetNewStringTwoLock service = new SetNewStringTwoLock();
        Thread a = new Thread() {
            @Override
            public void run() {
                service.testMethod();
            }
        };
        a.setName("A");
        a.start();
        Thread b = new Thread() {
            @Override
            public void run() {
                service.testMethod();
            }
        };
        b.setName("B");
        Thread.sleep(50);
        //加上这句，在b执行时锁对象已经变了，所以就是两个锁对象了，就是异步执行了
        //没有这里的sleep时，A和B都是在抢123锁，所以是同步
        b.start();
    }
}
