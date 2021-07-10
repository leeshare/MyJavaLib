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
 * 对于前面 SingleReactor 只能运行于单线程的问题，
 * 为了可以充分利用系统资源，运行于多个CPU的机器, MthreadReactor 将Reactor拆分为两部分。
 * Created by Administrator on 11/29/2019.
 */
public class MultiThreadReactor implements Runnable {
    //subReactors集合，一个selector代表一个subReactor
    Selector[] selectors = new Selector[2];
    int next = 0;
    final ServerSocketChannel serverSocket;

    MultiThreadReactor(int port) throws IOException {
        //Reactor初始化
        selectors[0] = Selector.open();
        selectors[1] = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        //非阻塞
        serverSocket.configureBlocking(false);

        //分步处理，第一步，接收accept事件
        SelectionKey sk = serverSocket.register(selectors[0], SelectionKey.OP_ACCEPT);
        //attach callback object, Acceptor
        sk.attach(new MyAcceptor());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                for (int i = 0; i < 2; i++) {
                    selectors[i].select();
                    Set selected = selectors[i].selectedKeys();
                    Iterator it = selected.iterator();
                    while (it.hasNext()) {
                        //Reactor负责dispatch收到的事件
                        myDispatch((SelectionKey) (it.next()));
                    }
                    selected.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void myDispatch(SelectionKey k) {
        Runnable r = (Runnable) (k.attachment());
        //调用之前注册的callback对象
        if (r != null) {
            r.run();
        }
    }

    class MyAcceptor {
        public synchronized void run() throws IOException {
            //主selector负责accept
            SocketChannel connection = serverSocket.accept();
            if (connection != null) {
                //选个 subReactor去负责接收到的connection
                //new SingleThreadHandler(selectors[next], connection);
                new MultiThreadHandler(selectors[next], connection);
            }
            if (++next == selectors.length) {
                next = 0;
            }
        }

    }

}
