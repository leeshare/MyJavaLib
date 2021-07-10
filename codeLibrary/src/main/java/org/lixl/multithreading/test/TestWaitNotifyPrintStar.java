package org.lixl.multithreading.test;

public class TestWaitNotifyPrintStar {
    public static class DBTools {
        volatile public boolean prevIsA = false;

        synchronized public void backupA() {
            try {
                while (prevIsA == true) {
                    wait();
                }
                for (int i = 0; i < 5; i++) {
                    System.out.println("★★★★★");
                }
                prevIsA = true;
                notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized public void backupB() {
            try {
                while (prevIsA == false) {
                    wait();
                }
                for (int i = 0; i < 5; i++) {
                    System.out.println("☆☆☆☆☆");
                }
                prevIsA = false;
                notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        DBTools dbTools = new DBTools();
        for (int i = 0; i < 20; i++) {
            new Thread() {
                @Override
                public void run() {
                    dbTools.backupA();
                }
            }.start();
            new Thread() {
                @Override
                public void run() {
                    dbTools.backupB();
                }
            }.start();
        }
    }
}
