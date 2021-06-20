import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lxl on 18/7/4.
 */
public class TestProducerConsumer5 {
    class Producer implements Runnable{
        private BlockingQueue<Integer> queue;
        private volatile Boolean isRunning = true;
        private AtomicInteger count = new AtomicInteger();

        public Producer(BlockingQueue<Integer> queue){
            this.queue = queue;
        }

        @Override
        public void run(){
            try{
                while(isRunning){
                    Integer data = count.incrementAndGet();
                    System.out.println("producer data=" + data);
                    queue.offer(data, 2, TimeUnit.SECONDS);
                    Thread.sleep(1000);
                }
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }

        }

        public void stop(){
            isRunning = false;
        }
    }

    class Consumer implements Runnable{
        private BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue){
            this.queue = queue;
        }

        @Override
        public void run(){
            try {
                while (true) {
                    Thread.sleep(1000);
                    Integer data = queue.take();
                    System.out.println("consumer data=" + data);
                }
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }

        }
    }

    public static void main(String[] args) throws InterruptedException{
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(5);
        TestProducerConsumer5 t = new TestProducerConsumer5();
        Producer p1 = t.new Producer(queue);
        Producer p2 = t.new Producer(queue);
        Consumer c1 = t.new Consumer(queue);
        Consumer c2 = t.new Consumer(queue);

        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(p1);
        pool.execute(p2);
        pool.execute(c1);
        pool.execute(c2);
        Thread.sleep(5000);
        p1.stop();
        p2.stop();
        Thread.sleep(2000);
        pool.shutdown();

    }
}
