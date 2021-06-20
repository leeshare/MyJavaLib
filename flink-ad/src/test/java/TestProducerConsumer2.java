import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lxl on 18/6/30.
 */
public class TestProducerConsumer2 {

    class Producer implements Runnable{
        BlockingQueue<Integer> queue;
        private AtomicInteger count = new AtomicInteger();
        private volatile Boolean isRunning = true;
        private static final int SLEEPTIME = 1000;

        public Producer(BlockingQueue<Integer> queue){
            this.queue = queue;
        }

        @Override
        public void run(){
            Random r = new Random();
            try {
                while(isRunning){
                    Thread.sleep(r.nextInt(SLEEPTIME));
                    int data = count.incrementAndGet();
                    System.out.println("生产 加入队列:" + data);
                    if(!queue.offer(data, 2, TimeUnit.SECONDS)){
                        System.out.println("生产 加入队列失败");
                    }
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        public void stop(){
            this.isRunning = false;
            System.out.println("生产 停止");
        }

    }
    class Consumer implements Runnable{
        BlockingQueue<Integer> queue;
        private static final int TIMESLEEP = 1000;

        public Consumer(BlockingQueue<Integer> queue){
            this.queue = queue;
        }

        @Override
        public void run(){
            Random r = new Random();
            try {
                System.out.println("消费 加入队列:");
                while(true) {
                    Integer i = queue.take();
                    if (!queue.isEmpty()) {
                        System.out.println("消费 数据" + i);
                    } else {
                        System.out.println("消费 队列已空");
                    }
                    Thread.sleep(r.nextInt(TIMESLEEP));
                }

            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException{
        BlockingQueue<Integer> queue = new LinkedBlockingDeque<>(5);
        TestProducerConsumer2 tpc = new TestProducerConsumer2();
        TestProducerConsumer2.Producer p1 = tpc.new Producer(queue);
        TestProducerConsumer2.Producer p2 = tpc.new Producer(queue);
        TestProducerConsumer2.Producer p3 = tpc.new Producer(queue);
        TestProducerConsumer2.Consumer c1 = tpc.new Consumer(queue);
        TestProducerConsumer2.Consumer c2 = tpc.new Consumer(queue);
        TestProducerConsumer2.Consumer c3 = tpc.new Consumer(queue);

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
        Thread.sleep(3000);
        pool.shutdown();

    }
}
