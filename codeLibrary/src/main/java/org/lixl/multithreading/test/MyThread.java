package org.lixl.multithreading.test;

/**
 * 测试 this 和 Thread.currentThread() 的区别
 */
public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println("run() --begin");
        System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
        System.out.println("Thread.currentThread().isAlive() = " + Thread.currentThread().isAlive());
        System.out.println("Thread.currentThread().getId() = " + Thread.currentThread().getId());
        System.out.println("this.getName() = " + this.getName());
        System.out.println("this.isAlive() = " + this.isAlive()); //这个this指什么？
        System.out.println("this.getId() = " + this.getId());
        System.out.println("run() --end");
    }

    public MyThread() {
        System.out.println("MyThread --begin");
        System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
        System.out.println("Thread.currentThread().isAlive() = " + Thread.currentThread().isAlive());
        System.out.println("Thread.currentThread().getId() = " + Thread.currentThread().getId());
        System.out.println("this.getName() = " + this.getName());
        System.out.println("this.isAlive() = " + this.isAlive());   //构造时，MyThread线程未启用，所以是false
        System.out.println("this.getId() = " + this.getId());
        System.out.println("MyThread --end");

    }

    public static void main(String[] args) throws Exception {
        MyThread myThread = new MyThread();
        Thread t1 = new Thread(myThread);
        System.out.println("main begin t1 isAlive = " + t1.isAlive());
        t1.setName("A");
        t1.start();
        System.out.println("main end t1 isAlive =" + t1.isAlive());
    }
}
