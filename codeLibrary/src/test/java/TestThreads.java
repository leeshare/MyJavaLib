import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 交替打印A1B2C3-Java多线程实现方式
 */
public class TestThreads {

    static char[] numbers = "12345".toCharArray();
    static char[] characters = "abcde".toCharArray();

    static Thread t1, t2;
    /**
     * 1、通过 LockSupport 实现
     *      park()来阻塞当前线程；
     *      unpark(t2) 来唤醒对应线程；
     */
    private static void lockSupportMethod() {
        System.out.println("=============LockSupport实现===============");
        t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(char c : numbers) {
                    System.out.println(c);
                    LockSupport.unpark(t2);
                    LockSupport.park();
                }
            }
        });
        t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(char c : characters) {
                    LockSupport.park();
                    System.out.println(c);
                    //t2.wait();
                    LockSupport.unpark(t1);
                }
            }
        });

        t1.start();
        t2.start();
    }

    enum ReadyToRun{T1, T2}
    static volatile ReadyToRun r = ReadyToRun.T1;
    /**
     * 2、使用CAS自旋锁 + volatile
     */
    private static void casAndVolatileMethod() {

        System.out.println("=============CAS & Volatile实现===============");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(char c : numbers){
                    while (r != ReadyToRun.T1){
                    }
                    System.out.println(c);
                    r = ReadyToRun.T2;
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(char c : characters){
                    while (r != ReadyToRun.T2){
                    }
                    System.out.println(c);
                    r = ReadyToRun.T1;
                }
            }
        }).start();

    }

    private static AtomicInteger threadNo = new AtomicInteger(1);

    /**
     *3 使用CAS自旋锁 + AtomicInteger
     */
    private static void casAndAtomicIntegerMethod(){
        System.out.println("=============CAS & AtomicInteger实现===============");

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(char c : numbers){
                    while(threadNo.get() != 1){ }
                    System.out.println(c);
                    threadNo.set(2);
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(char c : characters){
                    while(threadNo.get() != 2){ }
                    System.out.println(c);
                    threadNo.set(1);
                }
            }
        }).start();
    }

    /**
     * 4、使用 wait and notify
     */
    private static void waitAndNotifyMethod(){
        final Object o = new Object();
        System.out.println("=============Wait & Notify 实现===============");
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o) {
                    for (char c : numbers) {
                        try {
                            System.out.println(c);
                            o.wait();
                            o.notify();
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o) {
                    for (char c : characters) {
                        System.out.println(c);
                        o.notify();
                        try {
                            o.wait();
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }

                    }
                    o.notify();
                }
            }
        }).start();
    }

    public static void main(String[] args) throws Exception{
        //lockSupportMethod();
        //casAndVolatileMethod();
        //casAndAtomicIntegerMethod();
        waitAndNotifyMethod();
    }
}
