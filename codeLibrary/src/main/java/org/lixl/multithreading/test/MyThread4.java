package org.lixl.multithreading.test;

/**
 * suspend 被废除的原因
 */
public class MyThread4 extends Thread {
    private long i = 0;

    @Override
    public void run() {
        while (true) {
            i++;
            //加上这句话后，main()中的打印就不会出现了。
            System.out.println(i);
            /**
             * 原因是： synchronized (this) {
             *             print(x);
             *             newLine();
             *         }
             * 当使用 suspend()时，println中的同步锁未被释放！！！
             * */
        }
    }

    public static void main(String[] args) {
        try {
            MyThread4 t = new MyThread4();
            t.start();
            Thread.sleep(1000);
            t.suspend();
            System.out.println("main end!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
