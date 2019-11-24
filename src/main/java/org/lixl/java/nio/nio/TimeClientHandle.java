package org.lixl.java.nio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 11/24/2019.
 */
public class TimeClientHandle implements Runnable {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public TimeClientHandle(String host, int port) {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while(!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                SelectionKey key = null;
                while(it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if(key != null) {
                            key.cancel();
                            if(key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        //多路复用器关闭后，所有注册在上面的Channel 和 Pipe 等资源都会被自动 去注册 并关闭，所以不需要重复释放资源
        if(selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()) {
            //判断是否连接成功
            SocketChannel sc = (SocketChannel) key.channel();
            if(key.isConnectable()) {
                if(sc.finishConnect()) {
                    //说明客户端连接成功

                    //这里同 doConnect 连接成功后 一样的操作
                    sc.register(selector, SelectionKey.OP_READ);
                    doWrite(sc);
                } else {
                    System.exit(1); //连接失败，进程退出
                }
            }
            if(key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if(readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("Now is : " + body);
                    this.stop = true;
                } else if(readBytes < 0) {
                    key.cancel();
                    sc.close();
                } else {

                }
            }
        }

    }

    private void doConnect() throws IOException {
        //如果直接连接成功，则注册到多路复用器上，发送请求消息，读应答
        if(socketChannel.connect(new InetSocketAddress(host, port))) {
            //炼成成功，则将 SocketChannel注册到多路复用器Selector上，注册SelectionKey.OP_READ
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        } else {
            //没有直接连接成功，则说明服务器没有返回TCP握手应答消息，但这不代表连接失败
            //我们需要将 SocketChannel注册到多路复用器Selector上，注册SelectionKey.OP_CONNECT
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }

    }

    private void doWrite(SocketChannel sc) throws IOException {
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        sc.write(writeBuffer);
        //对发送结果进行判断
        if(!writeBuffer.hasRemaining()) {
            //缓冲区中的消息全部发送完成
            System.out.println("Send order 2 server succeed.");
        }

        //由于发送时异步的，所以会存在“半包写”问题
    }

}
