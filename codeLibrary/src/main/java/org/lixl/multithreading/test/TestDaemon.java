package org.lixl.multithreading.test;

/**
 * 守护线程就是用来守护 非守护线程的
 * 当非守护线程退出，则守护线程自动销毁。
 * <p>
 * 如果不设置成守护线程，则会一直运行，而不会销毁
 */
public class TestDaemon extends Thread {
    private int i = 0;

    @Override
    public void run() {
        try {
            while (true) {
                i++;
                System.out.println("i=" + (i));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TestDaemon t = new TestDaemon();
        t.setDaemon(true);
        t.start();
        Thread.sleep(5000);
        System.out.println("我离开thread对象也不再打印了，也就是停止了");
    }
}
