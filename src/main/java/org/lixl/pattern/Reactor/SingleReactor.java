package org.lixl.pattern.Reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * "Scalable IO in java" 实现了一个单线程Reactor
 * Created by Administrator on 11/29/2019.
 */
public class SingleReactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel serverSocket;

    SingleReactor(int port) throws IOException {
        //Reactor初始化
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        //非阻塞
        serverSocket.configureBlocking(false);

        //分步处理，第一步，接收accept事件
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        //attach callback object, Acceptor
        sk.attach(new MyAcceptor());
    }

    void myDispatch(SelectionKey k) {
        Runnable r = (Runnable) (k.attachment());
        //调用之前注册的callback对象
        if(r != null) {
            r.run();
        }
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while(it.hasNext()) {
                    //Reactor负责dispatch收到的事件
                    myDispatch((SelectionKey) (it.next()));
                }
                selected.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //inner class
    class MyAcceptor implements Runnable {

        @Override
        public void run() {
            try {
                SocketChannel channel = serverSocket.accept();
                if(channel != null) {
                    new SingleHandler(selector, channel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
