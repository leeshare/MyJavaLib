import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁的使用  ReadWriteLock
 * Created by Administrator on 8/8/2019.
 */
public class TestReadWriteLock {
    static ReadWriteLock lock = new ReentrantReadWriteLock();
    static Lock read = lock.readLock();
    static Lock write = lock.writeLock();
    static HashMap<String, Object> map = new HashMap<>();
    static Object get(String key){
        read.lock();
        //读锁：允许其他读锁同时访问
        try {
            return map.get(key);
        } finally {
            read.unlock();
        }
    }
    static void put(String key, Object value){
        write.lock();
        //写锁：排除其他读锁和写锁
        try {
            map.put(key, value);
        } finally {
            write.unlock();
        }
    }
    static void clear(){
        write.lock();
        try{
            map.clear();
        } finally {
            write.unlock();
        }
    }
}
