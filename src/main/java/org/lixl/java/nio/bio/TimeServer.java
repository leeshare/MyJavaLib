package org.lixl.java.nio.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 同步阻塞服务端 示例 TimeServer
 * Created by Administrator on 11/23/2019.
 */
public class TimeServer {
    private Logger log = LoggerFactory.getLogger(TimeServer.class);

    public static void main(String[] args) throws IOException {
        int port = 8080;
        if(args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                //采用默认值
            }
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("此时间服务器启动端口是：" + port);
            Socket socket = null;
            while(true) {
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }

        } finally {
            if(server != null) {
                System.out.println("The time server close");
                server.close();
                server = null;
            }

        }
    }
}
