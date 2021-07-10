package org.lixl.multithreading.test;

/**
 * interrupt()
 * interrupted()
 * isInterrupted()
 */
public class MyThreadInterrupt extends Thread {

    @Override
    public void run() {
        super.run();
        try {
            //this.stop();
            for (int i = 0; i < 100000; i++) {
                if (this.interrupted())
                //if(this.isInterrupted())
                {
                    System.out.println("已停止状态，退出");
                    //break;
                    //throw new InterruptedException();

                    return;
                }
                System.out.println("i=" + (i + 1));
            }
            System.out.println("begin");
            Thread.sleep(10000);
            System.out.println("我被输出，如果此代码时for又继续运行，线程并未停止");
        } catch (InterruptedException e) {
            System.out.println("在沉睡中被停止！进入catch！" + this.isInterrupted());
            e.printStackTrace();
        } catch (ThreadDeath e) {
            System.out.println("ThreadDeath异常");
        }
    }

    public static void main(String[] args) throws Exception {
        MyThreadInterrupt thread = new MyThreadInterrupt();
        thread.start();
        //Thread.sleep(2000);

        thread.interrupt();
        System.out.println("是否停止1？" + thread.isInterrupted());
        System.out.println("是否停止1？" + thread.isInterrupted());
        //System.out.println("是否停止2？" + thread.interrupted());
        //System.out.println("是否停止2？" + thread.interrupted());


    }
}
