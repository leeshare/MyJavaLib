package org.lixl.multithreading.test;

/**
 * 延迟加载/懒汉模式
 * 使用DCL 双检查锁机制 来实现
 */
public class Singleton {
    public static class MyObject {
        private volatile static MyObject myObject;

        private MyObject() {
        }

        public static MyObject getInstance() {
            try {
                if (myObject != null) {

                } else {
                    Thread.sleep(3000);
                    synchronized (MyObject.class) {
                        if (myObject == null) {
                            myObject = new MyObject();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return myObject;
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                System.out.println(MyObject.getInstance().hashCode());
            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run() {
                System.out.println(MyObject.getInstance().hashCode());
            }
        };
        Thread t3 = new Thread() {
            @Override
            public void run() {
                System.out.println(MyObject.getInstance().hashCode());
            }
        };
        t1.start();
        t2.start();
        t3.start();
    }
}
