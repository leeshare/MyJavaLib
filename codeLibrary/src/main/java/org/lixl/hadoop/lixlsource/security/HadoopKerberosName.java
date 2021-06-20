package org.lixl.hadoop.lixlsource.security;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.security.authentication.util.KerberosName;

@SuppressWarnings("all")
@InterfaceAudience.LimitedPrivate({"HDFS", "MapReduce"})
@InterfaceStability.Evolving
public class HadoopKerberosName extends KerberosName {


    public HadoopKerberosName(String name) {
        super(name);
    }

}
