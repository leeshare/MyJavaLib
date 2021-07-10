package org.lixl.multithreading.test;

/**
 * suspend 和 resume的 正常用法
 */
public class MyThread3 extends Thread {
    private long i = 0;

    public long getI() {
        return i;
    }

    public void setI(long i) {
        this.i = i;
    }

    @Override
    public void run() {
        while (true) {
            i++;
        }
    }


    public static void main(String[] args) {
        try {
            MyThread3 thread = new MyThread3();
            thread.start();
            Thread.sleep(5000);
            //A段
            thread.suspend();
            System.out.println("A=" + System.currentTimeMillis() + " i=" + thread.getI());
            Thread.sleep(5000);
            System.out.println("A=" + System.currentTimeMillis() + " i=" + thread.getI());
            //B
            thread.resume();
            Thread.sleep(5000);
            //C
            thread.suspend();
            System.out.println("B=" + System.currentTimeMillis() + " i=" + thread.getI());
            Thread.sleep(5000);
            System.out.println("B=" + System.currentTimeMillis() + " i=" + thread.getI());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
