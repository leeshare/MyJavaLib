package org.lixl.hadoop.lixlsource.net.unix;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang.SystemUtils;
import org.apache.hadoop.util.NativeCodeLoader;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * 这是用java实现的 Unix的域套接字（domain socket）
 * Created by Administrator on 1/15/2020.
 */
@InterfaceAudience.LimitedPrivate("HDFS")
public class DomainSocket implements Closeable {
    static {
        if(SystemUtils.IS_OS_WINDOWS) {
            loadingFailureReason = "Unix域套接字在Windows上不可用。";
        } else if(!NativeCodeLoader.isNativeCodeLoaded()) {
            loadingFailureReason = "hadoop库无法加载";
        } else {
            String problem;
            try {
                anchorNative();
                problem = null;
            } catch (Throwable t) {
                problem = "域套接字#anchorNative出错：" + t.getMessage();
            }
            loadingFailureReason = problem;
        }
    }

    static final Logger LOG = LoggerFactory.getLogger(DomainSocket.class);

    /**
     * 若我们需要验证使用中的路径，则只能返回true
     */
    private static boolean validateBindPaths = true;

    /**
     * 域套接字不可用的原因
     */
    private final static String loadingFailureReason;

    /**
     * 初始化原生库代码
     */
    private static native void anchorNative();

    /**
     * 这个方法被设计用于验证一个选中的Unix域套接字的路径是否安全。
     * 这样的套接字路径是安全的：若其不允许无权限用户执行一个中间人攻击它。
     * @param path
     * @param skipComponents
     * @throws IOException
     */
    @VisibleForTesting
    native static void validateSocketPathSecurity0(String path, int skipComponents) throws IOException;

    /**
     * 不允许验证此服务绑定的路径
     */
    @VisibleForTesting
    public static void disableBindPathValidation() {
        validateBindPaths = false;
    }

    /**
     * 给定一个路径和端口，通过替换“_Port”为此端口来计算得有效路径
     * @param path
     * @param port
     * @return
     */
    public static String getEffectivePath(String path, int port) {
        return path.replace("_PORT", String.valueOf(port));
    }

    final CloseableRefrenceCount

}
