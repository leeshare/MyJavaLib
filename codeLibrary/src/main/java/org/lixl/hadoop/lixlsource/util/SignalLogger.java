package org.lixl.hadoop.lixlsource.util;

import org.apache.commons.logging.Log;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

@InterfaceAudience.Private
@InterfaceStability.Unstable
public enum  SignalLogger {
    INSTANCE;

    public void register(final Log LOG) {
        register(LogAdapter.create(LOG));
    }

    void register(final LogAdapter LOG) {
        //...
    }
}
