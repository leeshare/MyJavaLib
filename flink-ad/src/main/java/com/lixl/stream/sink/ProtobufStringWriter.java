package com.lixl.stream.sink;

import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import org.apache.flink.streaming.connectors.fs.StreamWriterBase;
import org.apache.flink.streaming.connectors.fs.Writer;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

public class ProtobufStringWriter<T extends Message> extends StreamWriterBase<T> {
    private static final long serialVersionUID = 1L;

    private String charsetName;
    private transient Charset charset;

    public ProtobufStringWriter(){
        this("UTF-8");
    }
    public ProtobufStringWriter(String charsetName) {
        this.charsetName = charsetName;
    }
    public ProtobufStringWriter(ProtobufStringWriter<T> other) {
        super(other);
        this.charsetName = other.charsetName;
    }

    @Override
    public void open(FileSystem fs, Path path) throws IOException {
        super.open(fs, path);
        try {
            this.charset = Charset.forName(charsetName);
        } catch (IllegalCharsetNameException e) {
            throw new IOException("The charset " + charsetName + " is not valid.", e);
        } catch (UnsupportedOperationException e) {
            throw new IOException("The charset " + charsetName + " is not supported.", e);
        }
    }

    /**
     * 这里要把 ProtoBuf 转成 Json 然后写文件
     *
     * 为什么要转呢？因为如果直接 protoBuf 写文件会有问题，出现一堆奇怪的东西
     * @param element
     * @throws IOException
     */
    @Override
    public void write(T element) throws IOException {
        FSDataOutputStream outputStream = getStream();
        outputStream.write(JsonFormat.printToString(element).getBytes(charset));
        outputStream.write('\n');
    }

    @Override
    public Writer<T> duplicate() {
        return new ProtobufStringWriter<>(this);
    }
}
