package org.lixl.hadoop.lixlsource.metrics2.util;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;

/**
 * 帮助计算运行样例统计
 */
@InterfaceAudience.Private
public class SampleStat {
    private final MinMax minmax = new MinMax();
    private long numSamples = 0;
    private double a0, a1, s0, s1, total;

    public SampleStat() {
        a0 = s0 = 0.0;
        total = 0.0;
    }

    public void reset() {
        numSamples = 0;
        a0 = s0 = 0.0;
        total = 0.0;
        minmax.reset();
    }

    void reset(long numSamples, double a0, double a1, double s0, double s1,
               double total, MinMax minmax) {
        this.numSamples = numSamples;
        this.a0 = a0;
        this.a1 = a1;
        this.s0 = s0;
        this.s1 = s1;
        this.total = total;
        this.minmax.reset(minmax);
    }

    public void copyTo(SampleStat other) {
        other.reset(numSamples, a0, a1, s0, s1, total, minmax);
    }

    public SampleStat add(double x) {
        minmax.add(x);
        return add(1, x);
    }

    public SampleStat add(long nSamples, double x) {
        numSamples += nSamples;
        total += x;

        if(numSamples == 1) {
            a0 = a1 = x;
            s0 = 0.0;
        } else {
            a1 = a0 + (x - a0) / numSamples;
            s1 = s0 + (x - a0) * (x - a1);
            a0 = a1;
            s0 = s1;
        }
        return this;
    }

    public long numSamples() {
        return numSamples;
    }

    public double total() {
        return total;
    }

    public double mean() {
        return numSamples > 0 ? (total / numSamples) : 0.0;
    }

    public double variance() {
        return numSamples > 1 ? s1 / (numSamples - 1) : 0.0;
    }

    public double stddev() {
        return Math.sqrt(variance());
    }

    public double min() {
        return minmax.min();
    }

    public double max() {
        return minmax.max();
    }

    @Override
    public String toString() {
        try {
            return "Samples=" + numSamples() +
                    " Min=" + min() +
                    " Mean=" + mean() +
                    " Std Dev=" + stddev() +
                    " Max=" + max();
        } catch (Throwable t) {
            return super.toString();
        }
    }


    /**
     * 帮助保持运行 min/max
     */
    @SuppressWarnings("PublicInnerClass")
    public static class MinMax {
        //Float.MAX_VALUE 使用超过 Double.MAX_VALUE，尽管最小和最大变量都是double类型。
        //Float.MAX_VALUE 足够大，并且使用 Double.MAX_VALUE 会导致 Ganglia core due to 缓存溢出。
        //同样原因应用于 MIN_VALUE
        static final double DEFAULT_MIN_VALUE = Float.MAX_VALUE;
        static final double DEFAULT_MAX_VALUE = Float.MIN_VALUE;
        // 对于这个代码和上面的解释 还是完全不明白

        private double min = DEFAULT_MIN_VALUE;
        private double max = DEFAULT_MAX_VALUE;

        public void add(double value) {
            if(value > max)
                max = value;
            if(value < min)
                min = value;
        }

        public double min() {
            return min;
        }
        public double max() {
            return max;
        }

        public void reset() {
            min = DEFAULT_MIN_VALUE;
            max = DEFAULT_MAX_VALUE;
        }

        public void reset(MinMax other) {
            min = other.min();
            max = other.max();
        }

    }
}
