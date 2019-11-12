package org.lixl.hadoop.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

import java.io.IOException;

/**
 * Created by Administrator on 11/12/2019.
 */
public class MyRpcServer {

    public static void main(String[] args) throws IOException {
        Server server = new RPC.Builder(new Configuration()).setProtocol(ClientProtocol.class)
                .setInstance(new ClientProtocolImpl())
                .setBindAddress("127.0.0.1")
                .setPort(8787)
                .setNumHandlers(5)
                .build();
        server.start();
    }

}
