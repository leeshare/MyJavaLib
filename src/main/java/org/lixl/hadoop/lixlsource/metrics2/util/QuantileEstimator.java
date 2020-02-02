package org.lixl.hadoop.lixlsource.metrics2.util;

import java.util.Map;

/**
 * 分位数估算器接口
 * Created by lxl on 20/2/2.
 */
public interface QuantileEstimator {
    void insert(long value);

    Map<Quantile, Long> snapshot();

    long getCount();

    void clear();
}
