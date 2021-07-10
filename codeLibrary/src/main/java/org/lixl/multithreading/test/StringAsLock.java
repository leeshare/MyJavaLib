package org.lixl.multithreading.test;

public class StringAsLock {

    public static void main(String[] args) {

        String a = "a";
        String b = "a";
        //打印 true  ————数据类型String的常量池特征
        System.out.println(a == b);
        String aa = new String("aa");
        String bb = new String("aa");
        System.out.println(aa == bb);

        Object obj = new Object();
        StringAsLock service = new StringAsLock();
        Thread t1 = new Thread() {
            @Override
            public void run() {
                //service.print("AA");
                service.print(aa);
            }
        };
        Thread t2 = new Thread() {

            @Override
            public void run() {
                //service.print("AA");
                service.print(bb);
            }
        };
        t1.setName("A");
        t1.start();
        t2.setName("B");
        t2.start();
    }

    public static void print(Object param) {
        try {
            synchronized (param) {
                while (true) {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(1000);

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
