package org.lixl.hadoop.lixlsource.metrics2;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MetricsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MetricsException(String message) {
        super(message);
    }

    public MetricsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetricsException(Throwable cause) {
        super(cause);
    }

}
