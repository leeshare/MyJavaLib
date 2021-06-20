package org.lixl.hadoop.lixlsource.util;

import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

import java.io.IOException;
import java.io.InputStream;

@InterfaceStability.Evolving
public class ThreadUtil {

    public static void sleepAtLeastIgnoreInterrupts(long millis) {
        long start = Time.now();
        while (Time.now() - start < millis) {
            long timeToSleep = millis - (Time.now() - start);
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException ie) {
                //LOG.warn("当休眠时中断", ie);
            }
        }
    }

    public static InputStream getResourceAsStream(String resourceName) throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if(cl == null) {
            throw new IOException("无法读资源文件 '" + resourceName + "' 因为当前线程的类加载器为空");
        }
        return getResourceAsStream(cl, resourceName);
    }
    public static InputStream getResourceAsStream(ClassLoader cl, String resourceName) throws IOException {
        if(cl == null) {
            throw new IOException("无法读资源文件'" + resourceName + "' 因所给类加载器为空");
        }
        InputStream is = cl.getResourceAsStream(resourceName);
        if(is == null) {
            throw new IOException("无法读资源文件'" + resourceName + "'");
        }
        return is;
    }

}
