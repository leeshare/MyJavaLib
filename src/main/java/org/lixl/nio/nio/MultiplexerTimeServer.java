package org.lixl.nio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 11/24/2019.
 */
public class MultiplexerTimeServer implements Runnable {
    private Selector selector;
    private ServerSocketChannel servChannel;
    private volatile boolean stop;

    /**
     * 初始化多路复用器，绑定监听端口
     * @param port
     */
    public MultiplexerTimeServer(int port) {
        try {
            selector = Selector.open();
            servChannel = ServerSocketChannel.open();
            //设置连接为异步非阻塞模式
            servChannel.configureBlocking(false);
            //backlog = 1024        requested maximum length of the queue of incoming connections.
            servChannel.socket().bind(new InetSocketAddress(port), 1024);
            //注册 channel到 Selector
            servChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port : " + port);
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                //无论是否有读写事件发生，selector每隔1秒被唤醒一次
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
                        if(key != null){
                            key.cancel();
                            if(key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            }catch (Throwable t) {
                t.printStackTrace();
            }
        }

        //多路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
        if(selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()) {
            //处理新接入的请求信息
            if(key.isAcceptable()) {
                //Accept the new connection
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                //Add the new connection to the selector
                sc.register(selector, SelectionKey.OP_READ);
            }

            if(key.isReadable()) {
                //Read the data
                SocketChannel sc = (SocketChannel) key.channel();
                //创建一个ByteBuffer，开辟一个1MB的缓冲区
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                //然后调用SocketChannel的read方法读取请求码流
                int readBytes = sc.read(readBuffer);
                if(readBytes > 0) {
                    //读到了字节，对字节进行编解码

                    //将缓冲区当前的limit设置为position，position设置为0，用于后续对缓冲区的读取操作
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("The time serer receive order : " + body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                            ? new Date(System.currentTimeMillis()).toString()
                            : "BAD ORDER";
                    doWrite(sc, currentTime);
                }else if(readBytes < 0) {
                    //对端链路关闭
                    key.cancel();
                    sc.close();
                } else {
                    //读到0字节，忽略
                }
            }
        }

    }

    /**
     * 将应答消息异步发送给客户端
     */
    private void doWrite(SocketChannel channel, String response) throws IOException{
        if(response != null && response.trim().length() > 0) {
            //将字符串编码成数组
            byte[] bytes = response.getBytes();

            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);

            writeBuffer.put(bytes);
            //
            writeBuffer.flip();
            //将缓冲区的字符数组发出去
            channel.write(writeBuffer);

            //其实这里还需要对于“写半包”问题处理
            //通过 ByteBuffer 的 hasRemain() 方法判断消息是否发送完成
        }
    }


}
