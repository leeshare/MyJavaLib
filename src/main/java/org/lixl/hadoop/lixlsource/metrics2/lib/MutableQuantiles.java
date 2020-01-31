package org.lixl.hadoop.lixlsource.metrics2.lib;

import com.google.common.annotations.VisibleForTesting;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.util.Quantile;

/**
 * Created by lxl on 20/1/30.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MutableQuantiles extends MutableMetric {

    @VisibleForTesting
    public static final Quantile[] quantiles = {
            new Quantile(0.50, 0.050),
            new Quantile(0.75, 0.025),
            new Quantile(0.90, 0.010),
            new Quantile(0.95, 0.005),
            new Quantile(0.99, 0.001)
    };

}
