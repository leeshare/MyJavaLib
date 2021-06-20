package org.lixl.hadoop.lixlsource.net.unix;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang.SystemUtils;
import org.apache.hadoop.util.NativeCodeLoader;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.util.CloseableReferenceCount;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;

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

    //static final Logger LOG = LoggerFactory.getLogger(DomainSocket.class);

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

    public static String getLoadingFailureReason() {
        return loadingFailureReason;
    }

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

    /**
     * 套接字引用数 和 关闭状态 的二进制
     * Bit 30: 0 = open, 1 = closed.
     * Bit 29 to 0: 引用数
     */
    final CloseableReferenceCount refCount;

    /**
     * 这个文件描述关联到此UNIX域套接字
     */
    final int fd;

    /**
     * 这个路径关联这个UNIX域套接字
     */
    private final String path;

    /**
     * 这个输入流关联到这个套接字
     */
    private final DomainInputStream inputStream = new DomainInputStream();


    private final DomainOutputStream outputStream = new DomainOutputStream();

    /**
     * 这个Channel关联到这个套接字
     */
    private final DomainChannel channel = new DomainChannel();

    private DomainSocket(String path, int fd) {
        this.refCount = new CloseableReferenceCount();
        this.fd = fd;
        this.path = path;
    }

    private static native int bind0(String path) throws IOException;

    private void unreference(boolean checkClosed) throws ClosedChannelException {
        if(checkClosed) {
            refCount.unreferenceCheckClosed();
        } else {
            refCount.unreference();
        }
    }

    /**
     * 创建一个新的域套接字来监听这个给定的路径
     * @param path
     * @return
     * @throws IOException
     */
    public static DomainSocket bindAndListen(String path) throws IOException {
        if(loadingFailureReason != null) {
            throw new UnsupportedOperationException(loadingFailureReason);
        }
        if(validateBindPaths) {
            validateSocketPathSecurity0(path, 0);
        }
        int fd = bind0(path);
        return new DomainSocket(path, fd);
    }

    /**
     * 创建一个UNIX域套接字的配对，通过调用 socketpair(2) 来连接彼此
     * @return
     * @throws IOException
     */
    public static DomainSocket[] socketpair() throws IOException {
        int fds[] = socketpair0();
        return new DomainSocket[] {
                new DomainSocket("(anonymous0)", fds[0]),
                new DomainSocket("(anonymous1)", fds[1])
        };
    }

    private static native int[] socketpair0() throws IOException;

    private static native int accept0(int fd) throws IOException;

    /**
     * 接受一个新的UNIX域套接字的连接
     * @return
     * @throws IOException
     */
    public DomainSocket accept() throws IOException {
        refCount.reference();   //增加引用数
        boolean exc = true;
        try {
            DomainSocket ret = new DomainSocket(path, accept0(fd));
            exc = false;
            return ret;
        } finally {
            unreference(exc);
        }
    }

    private static native int connect0(String path) throws IOException;

    /**
     * 创建一个新的域套接字连接到给定的路径
     * @param path
     * @return
     * @throws IOException
     */
    public static DomainSocket connect(String path) throws IOException {
        if(loadingFailureReason != null) {
            throw new UnsupportedOperationException(loadingFailureReason);
        }
        int fd = connect0(path);
        return new DomainSocket(path, fd);
    }

    /**
     * 返回 true：如果文件描述当前是打开的
     * @return
     */
    public boolean isOpen() {
        return refCount.isOpen();
    }

    public String getPath() {
        return path;
    }

    public DomainInputStream getInputStream() {
        return inputStream;
    }

    public DomainOutputStream getOutputStream() {
        return outputStream;
    }

    public DomainChannel getChannel() {
        return channel;
    }

    public static final int SEND_BUFFER_SIZE = 1;
    public static final int RECEIVE_BUFFER_SIZE = 2;
    public static final int SEND_TIMEOUT = 3;
    public static final int RECEIVE_TIMEOUT = 4;

    private static native void setAttribute0(int fd, int type, int val) throws IOException;

    public void setAttribute(int type, int size) throws IOException {
        refCount.reference();
        boolean exc = true;
        try {
            setAttribute0(fd, type, size);
            exc = false;
        } finally {
            unreference(exc);
        }
    }

    private native int getAttribute0(int fd, int type) throws IOException;

    public int getAttribute(int type) throws IOException {
        refCount.reference();
        int attribute;
        boolean exc = true;
        try {
            attribute = getAttribute0(fd, type);
            exc = false;
            return attribute;
        } finally {
            unreference(exc);
        }
    }

    private static native void close0(int fd) throws IOException;

    private static native void closeFileDescriptor0(FileDescriptor fd) throws IOException;

    private static native void shutdown0(int fd) throws IOException;

    /**
     * 关闭这个套接字
     * @throws IOException
     */
    public void close() throws IOException {
        //设置这个关闭的二进制在这个域套接字
        int count;
        try {
            count = refCount.setClosed();
        } catch (ClosedChannelException e) {
            //说明其他人已关闭此套接字了
            return;
        }
        //等待所有引用都释放掉
        boolean didShutdown = false;
        boolean interrupted = false;
        while(count > 0) {
            if(!didShutdown) {
                try {
                    //在此套接字上调用关闭，将打断阻塞系统
                    //调用像 accept, write, read 都将开启不同的线程。
                    shutdown0(fd);
                } catch (IOException e) {
                    //LOG.error("关闭出错：", e);
                }
                didShutdown = true;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                interrupted = true;
            }
            count = refCount.getReferenceCount();
        }

        //在此刻，没有人引用这个文件描述（file descriptor） ，并且也没有人将可能再获取引用
        //我们现在就调用 close(2) 在这个文件描述
        //过了此刻，这个文件描述数将被其他再使用
        //尽管这个域套接字对象继续持有旧文件描述数，但我们再也不能使用了，因为已关闭了。
        close0(fd);
        if(interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 调用 shutdown(SHUT_RDWR)在这个UNIX域套接字
     * @throws IOException
     */
    public void shutdown() throws IOException {
        refCount.reference();
        boolean exc = true;
        try {
            shutdown0(fd);
        } finally {
            unreference(exc);
        }
    }

    private native static void sendFileDescriptors0(int fd, FileDescriptor descriptors[], byte jbuf[], int offset, int length) throws IOException;

    /**
     * 发送一些FileDescriptor对象到套接字另一边的进程去
     * @param descriptors
     * @param jbuf
     * @param offset
     * @param length
     * @throws IOException
     */
    public void sendFileDescriptors(FileDescriptor descriptors[], byte jbuf[], int offset, int length) throws IOException {
        refCount.reference();
        boolean exc = true;
        try {
            sendFileDescriptors0(fd, descriptors, jbuf, offset, length);
            exc = false;
        } finally {
            unreference(exc);
        }
    }

    private static native int receiveFileDescriptors0(int fd, FileDescriptor descriptors[], byte buf[], int offset, int length) throws IOException;

    public int recvFileInputStreams(FileInputStream streams[], byte buf[], int offset, int length) throws IOException {
        FileDescriptor descriptors[] = new FileDescriptor[streams.length];
        boolean success = false;
        for(int i = 0; i < streams.length; i++) {
            streams[i] = null;
        }
        refCount.reference();
        try {
            int ret = receiveFileDescriptors0(fd, descriptors, buf, offset, length);
            for(int i = 0, j = 0; i < descriptors.length; i++) {
                if(descriptors[i] != null) {
                    streams[j++] = new FileInputStream(descriptors[i]);
                    descriptors[i] = null;
                }
            }
            success = true;
            return ret;
        } finally {
            if(!success) {
                for(int i = 0; i < descriptors.length; i++) {
                    if(descriptors[i] != null) {
                        try {
                            closeFileDescriptor0(descriptors[i]);
                        } catch (Throwable t) {
                            //LOG.warn(t.toString());
                        }
                    } else if(streams[i] != null) {
                        try {
                            streams[i].close();
                        } catch (Throwable t) {
                            //LOG.warn(t.toString());
                        } finally {
                            streams[i] = null;
                        }
                    }
                }
            }
            unreference(!success);
        }

    }

    private native static int readArray0(int fd, byte b[], int off, int len) throws IOException;

    private native static int available0(int fd) throws IOException;

    private static native void write0(int fd, int b) throws IOException;

    private static native void writeArray0(int fd, byte b[], int offset, int length) throws IOException;

    private native static int readByteBufferDirect0(int fd, ByteBuffer dst, int position, int remaining) throws IOException;

    /**
     * 输入流用于UNIX域套接字
     */
    @InterfaceAudience.LimitedPrivate("HDFS")
    public class DomainInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            refCount.reference();
            boolean exc = true;
            try {
                byte b[] = new byte[1];
                int ret = DomainSocket.readArray0(DomainSocket.this.fd, b, 0, 1);
                exc = false;
                return (ret >= 0) ? b[0] : -1;
            } finally {
                unreference(exc);
            }
        }

        @Override
        public int read(byte b[], int off, int len) throws IOException {
            refCount.reference();
            boolean exc = true;
            try {
                int nRead = DomainSocket.readArray0(DomainSocket.this.fd, b, off, len);
                exc = false;
                return nRead;
            } finally {
                unreference(exc);
            }
        }

        @Override
        public int available() throws IOException {
            refCount.reference();
            boolean exc = true;
            try {
                int nAvailable = DomainSocket.available0(DomainSocket.this.fd);
                exc = false;
                return nAvailable;
            } finally {
                unreference(exc);
            }
        }

        @Override
        public void close() throws IOException {
            DomainSocket.this.close();
        }
    }

    /**
     * 输出流用于UNIX域套接字
     */
    @InterfaceAudience.LimitedPrivate("HDFS")
    public class DomainOutputStream extends OutputStream {
        @Override
        public void close() throws IOException {
            DomainSocket.this.close();
        }

        @Override
        public void write(int val) throws IOException {
            refCount.reference();
            boolean exc = true;
            try {
                byte b[] = new byte[1];
                b[0] = (byte)val;
                DomainSocket.writeArray0(DomainSocket.this.fd, b, 0, 1);
                exc = false;
            } finally {
                unreference(exc);
            }
        }

        @Override
        public void write(byte b[], int off, int len) throws IOException {
            refCount.reference();
            boolean exc = true;
            try {
                DomainSocket.writeArray0(DomainSocket.this.fd, b, off, len);
                exc = false;
            } finally {
                unreference(exc);
            }
        }
    }

    @InterfaceAudience.LimitedPrivate("HDFS")
    public class DomainChannel implements ReadableByteChannel {
        @Override
        public boolean isOpen() {
            return DomainSocket.this.isOpen();
        }

        @Override
        public void close() throws IOException {
            DomainSocket.this.close();
        }

        @Override
        public int read(ByteBuffer dst) throws IOException {
            refCount.reference();
            boolean exc = true;
            try {
                int nread = 0;
                if(dst.isDirect()) {
                    nread = DomainSocket.readByteBufferDirect0(DomainSocket.this.fd, dst, dst.position(), dst.remaining());
                } else if(dst.hasArray()) {
                    nread = DomainSocket.readArray0(DomainSocket.this.fd, dst.array(), dst.position(), dst.remaining());
                } else {
                    throw new AssertionError("我们不支持使用既不主导也不依靠数组的ByteBuffers");
                }
                if(nread > 0) {
                    dst.position(dst.position() + nread);
                }
                exc = false;
                return nread;
            } finally {
                unreference(exc);
            }
        }

    }

    @Override
    public String toString() {
        return String.format("DomainSocket(fd=%d, path=%s)", fd, path);
    }

}
