package org.lixl.multithreading.test;

/**
 * suspend 和 resume 出现因线程暂停而导致的数据不同步情况。
 */
public class MyObject {
    private String username = "1";
    private String password = "11";

    public void setValue(String u, String p) {
        this.username = u;
        if (Thread.currentThread().getName().equals("a")) {
            System.out.println("停止a线程");
            Thread.currentThread().suspend();
        }
        this.password = p;
    }

    public void printlnUP() {
        System.out.println(username + " " + password);
    }

    public static void main(String[] args) throws InterruptedException {
        final MyObject o = new MyObject();
        Thread t1 = new Thread() {
            @Override
            public void run() {
                o.setValue("a", "aa");
            }
        };
        t1.setName("a");
        t1.start();
        Thread.sleep(500);
        Thread t2 = new Thread() {
            @Override
            public void run() {
                o.printlnUP();
            }
        };
        t2.start();
    }
}
