package resume;

import java.util.Random;
import java.util.concurrent.*;

public class ProducerAndConsumer {

    public class Producer implements Runnable{
        BlockingQueue<Integer> queue;
        volatile Boolean isRunning = true;

        public Producer(BlockingQueue<Integer> queue){
            this.queue = queue;
        }

        public void run(){
            try {
                while(isRunning) {

                    int data = (new Random()).nextInt() * 1000;
                    queue.offer(data, 2, TimeUnit.SECONDS);
                    Thread.sleep(1000);
                }
            } catch(InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }


        }

        public void stop(){
            isRunning = false;
        }

    }

    public class Consumer implements Runnable{
        BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue){
            this.queue = queue;
        }

        public void run(){
            try {
                while (true) {
                    Thread.sleep(1000);
                    Integer data = queue.take();
                }
            } catch(InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(4);
        ProducerAndConsumer pc = new ProducerAndConsumer();
        Producer p1 = pc.new Producer(queue);
        Producer p2 = pc.new Producer(queue);
        Consumer c1 = pc.new Consumer(queue);
        Consumer c2 = pc.new Consumer(queue);

        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(p1);
        pool.execute(p2);
        pool.execute(c1);
        pool.execute(c2);
        Thread.currentThread().sleep(5 * 1000);
        p1.stop();
        p2.stop();
        Thread.currentThread().sleep(2 * 1000);
        pool.shutdown();
    }
}
