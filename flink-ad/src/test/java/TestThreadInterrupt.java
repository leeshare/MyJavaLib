import java.util.concurrent.TimeUnit;

public class TestThreadInterrupt {
    static Thread thread1;
    private static void method1(){
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(Thread.currentThread().isInterrupted()){
                        System.out.println("线程：接收到终端信息，中断线程。。。中断标志" + Thread.currentThread().isInterrupted());
                        break;
                    }
                    System.out.println("线程正在执行...");
                }
            }
        });
        thread1.start();

        new Thread(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                while(i < 20){
                    System.out.println(Thread.currentThread().getName() + "线程正在执行...");
                    if(i == 8){
                        System.out.println("设置线程中断...");
                        thread1.interrupt();
                    }
                    i++;
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private static volatile boolean mark = false;
    private static void method2(){
        new Thread(() -> {
            while (!mark) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println("线程执行内容中...");
            }
        }, "myThread").start();

        System.out.println("这是主线程");
        try{
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        mark = true;
        System.out.println("标记位改为：" + mark);

    }

    public static void main(String[] args) {
        //method1();
        method2();
    }
}
