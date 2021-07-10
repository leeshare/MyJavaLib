package org.lixl.hadoop.lixlsource.hdfs.net;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;

/**
 * Represents a connection to a peer.
 * Created by Administrator on 1/15/2020.
 */
@InterfaceAudience.Private
public interface Peer extends Closeable {

    ReadableByteChannel getInputStreamChannel();

    void setReadTimeout(int timeoutMs) throws IOException;

    int getReceiveBufferSize() throws IOException;

    boolean getTcpNoDelay() throws IOException;

    void setWriteTimeout(int timeoutMs) throws IOException;

    boolean isClosed();

    void close() throws IOException;

    String getRemoteAddressString();

    String getLocalAddressString();

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    boolean isLocal();

    //DomainSocket getDomainSocket();

    boolean hasSecureChannel();

}
