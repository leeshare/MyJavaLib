package org.lixl.hadoop.lixlsource.util;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.conf.Configurable;
import org.lixl.hadoop.lixlsource.conf.Configuration;

@InterfaceAudience.Public
@InterfaceStability.Evolving
public class ReflectionUtils {

    public static void setConf(Object theObject, Configuration conf) {
        if (conf != null) {
            if (theObject instanceof Configurable) {
                ((Configurable) theObject).setConf(conf);
            }
            //setJobConf(theObject, conf);
        }
    }
}
