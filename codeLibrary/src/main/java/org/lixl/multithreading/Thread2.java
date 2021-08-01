package org.lixl.multithreading;

public class Thread2 {
    private static class RunnableThread implements Runnable {
        @Override
        public void run() {
            System.out.println("内部线程run " + Thread.currentThread().getName());

        }
    }
    public static void main(String[] args) {
        RunnableThread in = new RunnableThread();
        Thread out = new Thread(in);
        out.setName("out");
        out.start();

    }
}
