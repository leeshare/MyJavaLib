package org.lixl.multithreading.test;

/**
 * 脏读
 */
public class DirtyRead {
    public String username = "A";
    public String password = "AA";

    synchronized public void setValue(String username, String password) {
        try {
            this.username = username;
            Thread.sleep(5000);
            this.password = password;
            System.out.println("setValue thread name = " + Thread.currentThread().getName() + " username = " + username + " password = " + password);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //读取 不加同步
    //synchronized
    public void getValue() {
        System.out.println("getValue thread name = " + Thread.currentThread().getName() + " username = " + username + " password = " + password);
    }

    public static void main(String[] args) throws InterruptedException {
        DirtyRead o = new DirtyRead();
        Thread a = new Thread() {
            @Override
            public void run() {
                o.setValue("B", "BB");

            }
        };
        a.start();
        Thread.sleep(200);
        o.getValue();
    }
}
