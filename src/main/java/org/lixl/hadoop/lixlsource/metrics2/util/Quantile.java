package org.lixl.hadoop.lixlsource.metrics2.util;

import com.google.common.collect.ComparisonChain;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;

/**
 * 具体的一个 quantile 作为被观察对象
 * Created by lxl on 20/1/31.
 */
@InterfaceAudience.Private
public class Quantile implements Comparable<Quantile> {
    public final double quantile;
    public final double error;

    public Quantile(double quantile, double error) {
        this.quantile = quantile;
        this.error = error;
    }

    @Override
    public boolean equals(Object aThat) {
        if(this == aThat) {
            return true;
        }
        if(!(aThat instanceof Quantile)) {
            return false;
        }

        Quantile that = (Quantile) aThat;

        long qbits = Double.doubleToLongBits(quantile);
        long ebits = Double.doubleToLongBits(error);

        return qbits == Double.doubleToLongBits(that.quantile)
                && ebits == Double.doubleToLongBits(that.error);
    }

    @Override
    public int hashCode() {
        return (int) (Double.doubleToLongBits(quantile) ^ Double.doubleToLongBits(error));
    }

    @Override
    public int compareTo(Quantile other) {
        return ComparisonChain.start()
                .compare(quantile, other.quantile)
                .compare(error, other.error)
                .result();
    }

    @Override
    public String toString() {
        return String.format("%.2f %%ile +/- %.2f%%",
                quantile * 100, error * 100);
    }
}
