package org.lixl.java.util;

import org.lixl.java.util.function.LConsumer;

import java.util.Objects;

/**
 * Created by lxl on 18/12/27.
 */
public interface LIterator<E> {

    boolean hasNext();

    E next();

    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    default void forEachRemaining(LConsumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }
}
