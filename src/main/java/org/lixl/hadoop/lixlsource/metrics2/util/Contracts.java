package org.lixl.hadoop.lixlsource.metrics2.util;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;

/**
 * 额外的帮助者 用于程序
 */
@InterfaceAudience.Private
public class Contracts {

    private Contracts() {

    }

    /**
     * 检查参数的错误条件
     * @param arg
     * @param expression
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> T checkAvg(T arg, boolean expression, Object msg) {
        if(!expression) {
            throw new IllegalArgumentException(String.valueOf(msg) + ": " + arg);
        }
        return arg;
    }

    public static int checkArg(int arg, boolean expression, Object msg) {
        if(!expression) {
            throw new IllegalArgumentException(String.valueOf(msg) + ": " + arg);
        }
        return arg;
    }

    public static long checkArg(long arg, boolean expression, Object msg) {
        if(!expression) {
            throw new IllegalArgumentException(String.valueOf(msg) + ": " + arg);
        }
        return arg;
    }

    public static float checkArg(float arg, boolean expression, Object msg) {
        if(!expression) {
            throw new IllegalArgumentException(String.valueOf(msg) + ": " + arg);
        }
        return arg;
    }

    public static double checkArg(double arg, boolean expression, Object msg) {
        if(!expression) {
            throw new IllegalArgumentException(String.valueOf(msg) + ": " + arg);
        }
        return arg;
    }
}
