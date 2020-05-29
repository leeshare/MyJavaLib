package org.lixl.hadoop.lixlsource.conf;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

/**
 * Created by lxl on 20/1/21.
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public interface Configurable {

    void setConf(Configuration conf);

    Configuration getConf();
}
