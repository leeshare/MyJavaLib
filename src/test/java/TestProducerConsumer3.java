import org.lixl.pattern.PCData;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lxl on 18/6/30.
 */
public class TestProducerConsumer3 {

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
        private AtomicInteger count = new AtomicInteger();
        private volatile Boolean isRunning = true;
        private final int TIMESLEEP = 1000;
        public Producer(BlockingQueue<PCData> queue){
            this.queue = queue;
        }

        @Override
        public void run() {
            System.out.println(String.format("生产 当前线程 %d", Thread.currentThread().getId()));
            Random r = new Random();
            PCData data = null;
            try {
                while(isRunning) {
                    Thread.sleep(r.nextInt(TIMESLEEP));
                    data = new PCData(count.incrementAndGet());
                    System.out.println(String.format("生产 数据%d", data.getData()));
                    queue.offer(data, 2, TimeUnit.SECONDS);
                }
            } catch(Exception e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        }

        public void stop(){
            isRunning = false;
            System.out.println("生产 停止");
        }
    }

    class Consumer implements Runnable {
        BlockingQueue<PCData> queue;
        private final int TIMESLEEP = 1000;
        public Consumer(BlockingQueue<PCData> queue){
            this.queue = queue;
        }

        @Override
        public void run() {
            System.out.println(String.format("消费 当前线程%d", Thread.currentThread().getId()));
            Random r = new Random();
            try {
                while(true){
                    PCData data = queue.take();
                    if(data != null && data.getData() > 0){
                        System.out.println(String.format("消费 数据%d", data.getData()));
                    }
                    Thread.sleep(r.nextInt(TIMESLEEP));
                }
            } catch(InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<PCData> queue = new LinkedBlockingQueue<PCData>(5);
        TestProducerConsumer3 t = new TestProducerConsumer3();
        TestProducerConsumer3.Producer p1 = t.new Producer(queue);
        TestProducerConsumer3.Producer p2 = t.new Producer(queue);
        TestProducerConsumer3.Producer p3 = t.new Producer(queue);
        TestProducerConsumer3.Consumer c1 = t.new Consumer(queue);
        TestProducerConsumer3.Consumer c2 = t.new Consumer(queue);
        TestProducerConsumer3.Consumer c3 = t.new Consumer(queue);

        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(p1);
        pool.execute(p2);
        pool.execute(p3);
        pool.execute(c1);
        pool.execute(c2);
        pool.execute(c3);
        Thread.sleep(10*1000);
        p1.stop();
        p2.stop();
        p3.stop();
        Thread.sleep(3*1000);
        pool.shutdown();

    }
}
