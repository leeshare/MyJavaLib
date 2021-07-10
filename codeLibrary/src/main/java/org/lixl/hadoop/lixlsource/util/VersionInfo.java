package org.lixl.hadoop.lixlsource.util;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@InterfaceAudience.Public
@InterfaceStability.Stable
public class VersionInfo {

    private Properties info;

    protected VersionInfo(String component) {
        info = new Properties();
        String versionInfoFile = component + "-version-info.properties";
        InputStream is = null;
        try {
            is = ThreadUtil.getResourceAsStream(VersionInfo.class.getClassLoader(), versionInfoFile);
            info.load(is);
        } catch (IOException ex) {
            //LoggerFactory.getLogger(getClass()).warn("无法读'" + versionInfoFile + "', " + ex.toString(), ex);
        } finally {
            //IOUtil.closeStream
        }
    }


    public static String getVersion() {
        //...
        return "";
    }

    public static String getUrl() {
        //return COMMON_VERSION_INFO._getUrl();
        return "";
    }

    public static String getRevision() {
        //return COMMON_VERSION_INFO._getRevision();
        return "";
    }

    public static String getDate() {
        //return COMMON_VERSION_INFO._getDate();
        return "";
    }

    /**
     * The user that compiled Hadoop.
     *
     * @return the username of the user
     */
    public static String getUser() {
        //return COMMON_VERSION_INFO._getUser();
        return "";
    }


}
