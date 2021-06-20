import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lxl on 18/6/30.
 */
public class TestProducerConsumer4 {
    private final int SLEEPTIME = 1000;

    class PCData {
        private final int data;
        public PCData(int data){
            this.data = data;
        }
        public int getData(){
            return data;
        }
    }

    class Producer implements Runnable {
        BlockingQueue<PCData> queue;
        volatile Boolean isRunning = true;
        AtomicInteger count = new AtomicInteger();

        public Producer(BlockingQueue<PCData> queue){
            this.queue = queue;
        }

        @Override
        public void run() {
            Random r = new Random();
            PCData data;
            System.out.println(String.format("生产 当前线程=%d", Thread.currentThread().getId()));
            try {
                while(isRunning){
                    data = new PCData(count.incrementAndGet());
                    if(!queue.offer(data, 2, TimeUnit.SECONDS)){
                        System.out.println("生产 异常");
                    }
                    Thread.sleep(r.nextInt(SLEEPTIME));
                }

            } catch(InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        }

        public void stop(){
            this.isRunning = false;
            System.out.println("生产 停止");
        }
    }

    class Consumer implements Runnable {
        BlockingQueue<PCData> queue;

        public Consumer(BlockingQueue<PCData> queue){
            this.queue = queue;
        }

        @Override
        public void run() {
            Random r = new Random();
            PCData data;
            try {
                while(true){
                    data = queue.take();
                    System.out.println(String.format("消费 数据=%d", data.getData()));
                    Thread.sleep(r.nextInt(SLEEPTIME));

                }

            } catch(InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<PCData> queue = new LinkedBlockingQueue<PCData>(5);
        Producer p1 = new TestProducerConsumer4().new Producer(queue);
        Producer p2 = new TestProducerConsumer4().new Producer(queue);
        Producer p3 = new TestProducerConsumer4().new Producer(queue);
        Consumer c1 = new TestProducerConsumer4().new Consumer(queue);
        Consumer c2 = new TestProducerConsumer4().new Consumer(queue);
        Consumer c3 = new TestProducerConsumer4().new Consumer(queue);

        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(p1);
        pool.execute(p2);
        pool.execute(p3);
        pool.execute(c1);
        pool.execute(c2);
        pool.execute(c3);
        Thread.sleep(5 * 1000);
        p1.stop();
        p2.stop();
        p3.stop();
        Thread.sleep(3 * 1000);
        pool.shutdown();

    }
}
