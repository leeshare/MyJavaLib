package org.lixl.nio.aio;

import java.io.IOException;

/**
 * Created by Administrator on 11/24/2019.
 */
public class TimeClient {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        if(args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-AsyncTimeClientHandler-001").start();
    }
}
