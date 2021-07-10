
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 11/8/2019.
 * ConcurrentHashMap 存在陷阱
 *      运行此例子，偶然会出现： 统计 yy个数 会少一个
 *      包括 main22 也是有问题
 *
 *      原因是 ConcurrentHashMap的每个方法 get put 都是原子操作，但放在一起就不是原子的了，就存在并发问题了：比如第一个线程刚执行完 get操作，还未加1，第二个线程就来了，导致第一个和第二个线程都拿到的key是一样的。
 *          解决方法要不就是使用 synchronized
 *              要不就是 AtomicInteger
 */

public class TestConcurrentHashMap {
    private static ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<String,Integer>();
    private static String[] array = {"yy","yy","welcome","java","234","java","1234","yy","welcome","java","234"};

    public static void main(String[] args) throws InterruptedException{

        System.out.println("array size:"+array.length);
        for (String str : array) {
            new Thread(new MyTask(str)).start();
        }

        for(Entry<String,Integer> entry : map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }

        main22(args);
        main_perfect(args);
    }

    static class MyTask implements Runnable{
        String key;

        public MyTask(String key) {
            this.key = key;
        }
        @Override
        public void run() {
            //map.putIfAbsent(key, value);
            Integer value = map.get(key);
            if (null == value) {
                map.put(key, 1);
            } else {
                map.put(key, value + 1);
            }
        }

    }

    public static void main22(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String,Integer>();
        map.put("key", 1);
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    int key = map.get("key") + 1; //step 1
                    map.put("key", key);//step 2
                }
            });
        }
        Thread.sleep(3000); //模拟等待执行结束
        System.out.println("------应该得到1001，实际得到：" + map.get("key") + "------");
        executorService.shutdown();
    }

    public static void main_perfect(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, AtomicInteger> map = new ConcurrentHashMap<String, AtomicInteger>();
        AtomicInteger integer = new AtomicInteger(1);
        map.put("key", integer);
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    //int key = map.get("key") + 1; //step 1
                    //map.put("key", key);//step 2
                    map.get("key").incrementAndGet();   //先加1再返回
                    //map.get("key").getAndIncrement();   //先返回再加1
                }
            });
        }
        Thread.sleep(3000); //模拟等待执行结束
        System.out.println("------应该得到1001，实际得到：" + map.get("key") + "------");
        executorService.shutdown();
    }

}
