package org.lixl.java.util.stream;

import org.lixl.java.lang.iface.LAutoCloseable;
import org.lixl.java.util.LIterator;

import java.util.Spliterator;

/**
 * 支持顺序操作 和 并发操作
 * Created by lxl on 18/12/27.
 * 两个参数 T 和 S, 我的理解是  T 就其他类型,比如字符串,而 S 则是 BaseStream的之类
 */
public interface LBaseStream<T, S extends LBaseStream<T, S>> extends LAutoCloseable {

    LIterator<T> iterator();

    Spliterator<T> spliterator();

    boolean isParallel();

    //返回顺序等效流
    S sequential();

    //返回平行等效流
    S parallel();

    //返回等效流
    S unordered();

    S onClose(Runnable closeHandler);

    @Override
    void close();
}
