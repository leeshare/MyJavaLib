package org.lixl.multithreading.test;

public class JoinTest {

    public static class MyThread extends Thread {
        @Override
        public void run() {
            try {
                int secondValue = (int) (Math.random() * 10000);
                System.out.println(secondValue);
                Thread.sleep(secondValue);

                System.out.println("等了这么多毫秒" + secondValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        MyThread thread = new MyThread();
        thread.start();
        //thread.run();

        //Thread.sleep(?);
        thread.join();
        System.out.println("我想等myThread对象执行完之后我再执行");
        System.out.println("但上面代码中的sleep()中的值该写多少呢");
        System.out.println("答案是 不确定");
    }
}
