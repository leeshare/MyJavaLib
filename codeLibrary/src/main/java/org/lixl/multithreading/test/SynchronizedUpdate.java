package org.lixl.multithreading.test;

/**
 * synchronized 也具有 volatile的功能 可见性
 * <p>
 * 在while中加入了 synchronized
 */
public class SynchronizedUpdate {
    private boolean isContinueRun = true;

    public void runMethod() {
        String anything = new String();
        while (isContinueRun == true) {
            synchronized (anything) {
            }
        }
        System.out.println("停下来了！");
    }

    public void stopMethod() {
        isContinueRun = false;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedUpdate service = new SynchronizedUpdate();
        Thread t1 = new Thread() {
            @Override
            public void run() {
                service.runMethod();
            }
        };
        t1.start();
        Thread.sleep(1000);
        Thread t2 = new Thread() {
            @Override
            public void run() {
                service.stopMethod();
            }
        };
        t2.start();
        System.out.println("已发出停止命令了");
    }
}
