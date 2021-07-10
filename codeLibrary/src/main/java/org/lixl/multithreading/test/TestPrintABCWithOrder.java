package org.lixl.multithreading.test;

public class TestPrintABCWithOrder {
    private Object obj = new Object();
    volatile public static int signal = 1;

    public void printA() throws InterruptedException {
        synchronized (obj) {
            while (signal != 1) {
                obj.wait();
            }
            System.out.println("A");
            signal = 2;
            obj.notifyAll();
        }
    }

    public void printB() throws InterruptedException {
        synchronized (obj) {
            while (signal != 2) {
                obj.wait();
            }
            System.out.println("B");
            signal = 3;
            obj.notifyAll();
        }
    }

    public void printC() throws InterruptedException {
        synchronized (obj) {
            while (signal != 3) {
                obj.wait();
            }
            System.out.println("C");
            signal = 1;
            obj.notifyAll();
        }
    }

    public static void main(String[] args) {
        TestPrintABCWithOrder service = new TestPrintABCWithOrder();

        new Thread() {
            @Override
            public void run() {
                try {
                    //while(true) {
                    service.printC();
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                try {
                    //while(true) {
                    service.printA();
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                try {
                    //while(true) {
                    service.printB();
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }
}
