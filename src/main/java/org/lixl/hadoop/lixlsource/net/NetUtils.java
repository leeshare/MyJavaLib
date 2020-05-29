package org.lixl.hadoop.lixlsource.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetUtils {

    public static String getHostname() {
        try {
            return "" + InetAddress.getLocalHost();
        } catch (UnknownHostException uhe) {
            return "" + uhe;
        }
    }
}
