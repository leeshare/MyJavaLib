package org.lixl.multithreading.test;

public class ThreadLocal1 {
    public static class Tools {
        public static ThreadLocal t1 = new ThreadLocal();

        /*public static void main(String[] args) {
            if(t1.get() == null){
                System.out.println("从未放过值");
                t1.set("我的值");
            }
            System.out.println(t1.get());
            System.out.println(t1.get());
        }*/
    }

    public static void main(String[] args) throws Exception {
        Thread a = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        Tools.t1.set("ThreadA" + (i + 1));
                        System.out.println("ThreadA get value=" + Tools.t1.get());
                        Thread.sleep(200);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        a.start();
        Thread b = new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        Tools.t1.set("ThreadB" + (i + 1));
                        System.out.println("ThreadB get value=" + Tools.t1.get());
                        Thread.sleep(200);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        b.start();

        for (int i = 0; i < 100; i++) {
            Tools.t1.set("Main" + (i + 1));
            System.out.println("Main get value=" + Tools.t1.get());
            Thread.sleep(200);
        }
    }
}
