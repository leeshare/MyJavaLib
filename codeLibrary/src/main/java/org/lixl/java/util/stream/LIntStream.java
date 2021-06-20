package org.lixl.java.util.stream;

import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

/**
 * Created by lxl on 18/12/27.
 */
public interface LIntStream extends LBaseStream<Integer, LIntStream>  {

    LIntStream filter(IntPredicate predicate);

    LIntStream map(IntUnaryOperator mapper);

    <U> LStream<U> mapToObject(IntFunction<? extends U> mapper);
}
