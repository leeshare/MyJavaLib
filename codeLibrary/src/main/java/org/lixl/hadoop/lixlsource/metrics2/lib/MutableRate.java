package org.lixl.hadoop.lixlsource.metrics2.lib;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

/**
 * 一个方便的可变 metric 用于测量产生量
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MutableRate extends MutableStat {

        //至少有一个缺省构造函数
    MutableRate(String name, String description, boolean extended) {
        super(name, description, "0ps", "Time", extended);
    }
}
