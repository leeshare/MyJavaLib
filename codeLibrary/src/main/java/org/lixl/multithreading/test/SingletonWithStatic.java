package org.lixl.multithreading.test;

/**
 * 使用静态块 或 静态变量 来实现单例线程安全
 */
public class SingletonWithStatic {
    public static class MyObject {
        private static MyObject instance = new MyObject();

        //private static MyObject instance = null;
        private MyObject() {
        }

        static {
            //instance = new MyObject();
        }

        public static MyObject getInstance() {
            return instance;
        }
    }

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println(MyObject.getInstance().hashCode());
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println(MyObject.getInstance().hashCode());
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println(MyObject.getInstance().hashCode());
                }
            }
        }.start();
    }
}
