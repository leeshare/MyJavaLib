package org.lixl.hadoop.lixlsource.io;

import org.apache.commons.logging.Log;
import org.apache.hadoop.fs.PathIOException;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.conf.Configuration;
import org.lixl.hadoop.lixlsource.util.Shell;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static org.lixl.hadoop.lixlsource.fs.CommonConfigurationKeysPublic.IO_FILE_BUFFER_SIZE_DEFAULT;
import static org.apache.hadoop.fs.CommonConfigurationKeysPublic.IO_FILE_BUFFER_SIZE_KEY;

@InterfaceAudience.Public
@InterfaceStability.Evolving
public class IOUtils {
    //public static final Logger LOG = LoggerFactory.getLogger(IOUtils.class);

    public static void copyBytes(InputStream in, OutputStream out, int buffSize, boolean close) throws IOException {
        try {
            copyBytes(in, out, buffSize);
            if (close) {
                out.close();
                out = null;
                in.close();
                in = null;
            }
        } finally {
            if (close) {
                closeStream(out);
                closeStream(in);
            }
        }
    }

    public static void copyBytes(InputStream in, OutputStream out, int buffSize) throws IOException {
        PrintStream ps = out instanceof PrintStream ? (PrintStream) out : null;
        byte buf[] = new byte[buffSize];
        int bytesRead = in.read(buf);
        while (bytesRead >= 0) {
            out.write(buf, 0, bytesRead);
            if ((ps != null) && ps.checkError()) {
                throw new IOException("无法写入到输出流中");
            }
            bytesRead = in.read(buf);
        }
    }

    public static void copyBytes(InputStream in, OutputStream out, Configuration conf) throws IOException {
        copyBytes(in, out, conf.getInt(IO_FILE_BUFFER_SIZE_KEY, IO_FILE_BUFFER_SIZE_DEFAULT), true);
    }

    public static void copyBytes(InputStream in, OutputStream out, Configuration conf, boolean close) throws IOException {
        copyBytes(in, out, conf.getInt(IO_FILE_BUFFER_SIZE_KEY, IO_FILE_BUFFER_SIZE_DEFAULT), close);
    }

    /**
     * 拷贝count个字节从一个流到另一个。
     *
     * @param in
     * @param out
     * @param count
     * @param close
     * @throws IOException
     */
    public static void copyBytes(InputStream in, OutputStream out, long count, boolean close) throws IOException {
        byte buf[] = new byte[4096];
        long bytesRemaining = count;
        int bytesRead;

        try {
            while (bytesRemaining > 0) {
                int bytesToRead = (int) (bytesRemaining < buf.length ? bytesRemaining : buf.length);
                bytesRead = in.read(buf, 0, bytesToRead);
                if (bytesRead == -1)
                    break;

                out.write(buf, 0, bytesRead);
                bytesRemaining -= bytesRead;
            }
            if (close) {
                out.close();
                out = null;
                in.close();
                in = null;
            }
        } finally {
            if (close) {
                closeStream(out);
                closeStream(in);
            }
        }
    }

    /**
     * 实用的包装用于从输入流读取。
     *
     * @param is
     * @param buf
     * @param off
     * @param len
     * @return
     * @throws IOException
     */
    public static int wrappedReadForCompressedData(InputStream is, byte[] buf, int off, int len) throws IOException {
        try {
            return is.read(buf, off, len);
        } catch (IOException ie) {
            throw ie;
        } catch (Throwable t) {
            throw new IOException("出错当读取压缩数据时", t);
        }
    }

    /**
     * 在一个循环中 读取len个字节
     *
     * @param in
     * @param buf
     * @param off
     * @param len
     * @throws IOException
     */
    public static void readFully(InputStream in, byte[] buf, int off, int len) throws IOException {
        int toRead = len;
        while (toRead > 0) {
            int ret = in.read(buf, off, toRead);
            if (ret < 0) {
                throw new IOException("提前结束在输入流");
            }
            toRead -= ret;
            off += ret;
        }
    }

    /**
     * 同 readFully()一样。在循环中跳过字节
     *
     * @param in
     * @param len
     * @throws IOException
     */
    public static void skipFully(InputStream in, long len) throws IOException {
        long amt = len;
        while (amt > 0) {
            long ret = in.skip(amt);
            if (ret == 0) {
                int b = in.read();
                if (b == -1) {
                    throw new EOFException("提前文件结束 从输入流在跳过" + (len - amt) + "字节之后");
                }
                ret = 1;
            }
            amt -= ret;
        }
    }

    /**
     * 关闭可关闭对象和忽略任何空指针。必须仅实用清除在异常柄中。
     *
     * @param log
     * @param closeables
     */
    @Deprecated
    public static void cleanup(Log log, java.io.Closeable... closeables) {
        for (java.io.Closeable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (Throwable e) {
                    if (log != null && log.isDebugEnabled()) {
                        log.debug("发生异常在关闭" + c, e);
                    }
                }
            }
        }
    }

    /*public static void cleanupWithLogger(Logger logger, java.io.Closeable... closeables) {
        for(java.io.Closeable c : closeables) {
            if(c != null) {
                try {
                    c.close();
                } catch (Throwable e) {
                    if(logger != null) {
                        logger.debug("异常在关闭{}", c, e);
                    }
                }
            }
        }
    }*/

    public static void closeStream(java.io.Closeable stream) {
        if (stream != null) {
//            cleanupWithLogger(null, stream);
        }
    }

    public static void closeStreams(java.io.Closeable... streams) {
        if (streams != null) {
//            cleanupWithLogger(null, streams);
        }
    }

    public static void closeSocket(Socket sock) {
        if (sock != null) {
            try {
                sock.close();
            } catch (IOException ignored) {
                //LOG.debug("忽略异常当关闭socket时", ignored);
            }
        }
    }

    public static class NullOutputStream extends OutputStream {
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
        }

        @Override
        public void write(int b) throws IOException {
        }
    }

    /**
     * 写一个字节缓冲区到一个可写字节通道，可处理短写
     *
     * @param bc
     * @param buf
     * @throws IOException
     */
    public static void writeFully(WritableByteChannel bc, ByteBuffer buf) throws IOException {
        do {
            bc.write(buf);
        } while (buf.remaining() > 0);
    }

    /**
     * 写一个字节缓存到一个文件通道在一个给定的偏移，可处理短写。
     *
     * @param fc
     * @param buf
     * @param offset
     * @throws IOException
     */
    public static void writeFully(FileChannel fc, ByteBuffer buf, long offset) throws IOException {
        do {
            offset += fc.write(buf, offset);
        } while (buf.remaining() > 0);
    }

    /**
     * 返回完成的文件列表字符串 在一个目录中
     *
     * @param dir
     * @param filter
     * @return
     * @throws IOException
     */
    public static List<String> listDirectory(File dir, FilenameFilter filter) throws IOException {
        ArrayList<String> list = new ArrayList<String>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir.toPath())) {
            for (Path entry : stream) {
                Path fileName = entry.getFileName();
                if (fileName != null) {
                    String fileNameStr = fileName.toString();
                    if ((filter == null) || filter.accept(dir, fileNameStr)) {
                        list.add(fileNameStr);
                    }
                }
            }
        } catch (DirectoryIteratorException e) {
            throw e.getCause();
        }
        return list;
    }

    public static void fsync(File fileToSync) throws IOException {
        if (!fileToSync.exists()) {
            throw new FileNotFoundException("File/Directory" + fileToSync.getAbsolutePath() + "不存在");
        }
        boolean isDir = fileToSync.isDirectory();

        if (isDir && Shell.WINDOWS) {
            return;
        }

        try (FileChannel channel = FileChannel.open(fileToSync.toPath(), isDir ? StandardOpenOption.READ : StandardOpenOption.WRITE)) {
            fsync(channel, isDir);
        }
    }

    public static void fsync(FileChannel channel, boolean isDir) throws IOException {
        try {
            channel.force(true);
        } catch (IOException ioe) {
            if (isDir) {
                assert !(Shell.LINUX || Shell.MAC) :
                        "在Linux和苹果上fsyncing一个目录 应该不抛出异常，我们不想依靠它在产品中。得到：" + ioe;
                return;
            }
            throw ioe;
        }
    }

    public static IOException wrapException(final String path, final String methodName, final IOException exception) {
        if (exception instanceof InterruptedIOException || exception instanceof PathIOException) {
            return exception;
        } else {
            String msg = String.format("失败 并且 %s 当处理文件/目录：[%s]在方法：[%s]",
                    exception.getClass().getName(), path, methodName);
            try {
                return wrapWithMessage(exception, msg);
            } catch (Exception ex) {
                return new PathIOException(path, exception);
            }
        }
    }

    private static <T extends IOException> T wrapWithMessage(final T exception, final String msg) throws T {
        Class<? extends Throwable> clazz = exception.getClass();
        try {
            Constructor<? extends Throwable> ctor = clazz.getConstructor(String.class);
            Throwable t = ctor.newInstance(msg);
            return (T) (t.initCause(exception));
        } catch (Throwable e) {
            //LOG.warn("未包裹异常类型" + clazz + ": 它没有构造器", e);
            throw exception;
        }
    }

    /**
     * 读取一个输入数据知道结束符然后返回一个字节数组。取保不要进入一个无限输入数据或永无返回。
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] readFullyToByteArray(DataInput in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while (true) {
                baos.write(in.readByte());
            }
        } catch (EOFException eof) {

        }
        return baos.toByteArray();
    }


}
