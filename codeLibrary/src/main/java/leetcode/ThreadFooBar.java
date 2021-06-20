package leetcode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * 第一种：synchronized + notify() + wait()  性能最高
 * 第二种：维护两个 SynchronousQueue            最低
 * 第三种：AtomicBoolean + ConcurrentHashMap + LockSupport 中等
 */
public class ThreadFooBar {
    public static void main(String[] args) {
    }

    private static int n;
    public ThreadFooBar(int n){
        this.n = n;
    }

    private static volatile int threadNo = 1;

    public static void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            while(threadNo != 1){}
            // printFoo.run() outputs "foo". Do not change or remove this line.
            printFoo.run();
            threadNo = 2;
        }
    }

    public static void bar(Runnable printBar) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            while(threadNo != 2){}
            // printBar.run() outputs "bar". Do not change or remove this line.
            printBar.run();
            threadNo = 1;
        }
    }


    public static class Test2 {
        private static int n;
        private static final AtomicBoolean b = new AtomicBoolean(true);
        private static final Map<String, Thread> map = new ConcurrentHashMap<>();

        public Test2(int n){
            this.n = n;
        }

        public static void foo(Runnable printFoo) throws InterruptedException {
            map.put("foo", Thread.currentThread());
            for(int i = 0; i < n; i++){
                while(!b.get()){
                    //b = false时，当前线程阻塞
                    LockSupport.park();
                }
                printFoo.run();
                b.compareAndSet(true, false);   //如果当前值是true，就改成false
                LockSupport.unpark(map.get("bar"));
            }
        }
        public static void bar(Runnable printBar) throws InterruptedException {
            map.put("bar", Thread.currentThread());
            for(int i = 0; i < n; i++){
                if(b.get()){
                    //b = true时，当前线程阻塞
                    LockSupport.park();
                }
                printBar.run();
                b.compareAndSet(false, true);
                LockSupport.unpark(map.get("foo"));
            }
        }
    }
}
