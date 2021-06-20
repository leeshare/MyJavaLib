package org.lixl.java.util.function;

import org.lixl.java.lang.iface.LFunctionalInterface;

import java.util.Objects;

/**
 * 用作lambda表达式
 * Created by lxl on 18/12/27.
 */
@LFunctionalInterface
public interface LConsumer<T> {

    void accept(T t);

    default LConsumer<T> andThen(LConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t);};
    }
}
