package org.lixl.java.nio.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 同步阻塞I/O 示例  TimeServerHandler
 * Created by Administrator on 11/23/2019.
 */
public class TimeServerHandler implements Runnable {
    private Logger log = LoggerFactory.getLogger(TimeServerHandler.class);
    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);
            String currentTime = null;
            String body = null;
            while(true) {
                body = in.readLine();
                if(body == null)
                    break;
                System.out.println("The time server receive order: " + body);
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ?
                        new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                out.println(currentTime);
            }
        } catch (Exception e) {
            if(in != null){
                try {
                    in.close();
                } catch (IOException el) {
                    el.printStackTrace();
                }
            }
            if(out != null){
                out.close();
                out = null;
            }
            if(this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException el) {
                    el.printStackTrace();
                }
                this.socket = null;
            }
        }

    }
}
