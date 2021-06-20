package org.lixl.hadoop.lixlsource.util;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

@InterfaceAudience.LimitedPrivate({"HBase"})
@InterfaceStability.Unstable
public class PlatformName {

    public static final String PLATFORM_NAME = (System.getProperty("os.name").startsWith("Windows")
            ? System.getenv("os") : System.getProperty("os.name"))
            + "-" + System.getProperty("os.arch") + "-" + System.getProperty("sun.arch.data.model");

    public static final String JAVA_VENDOR_NAME = System.getProperty("java.vendor");

    public static final boolean IBM_JAVA = JAVA_VENDOR_NAME.contains("IBM");
}
