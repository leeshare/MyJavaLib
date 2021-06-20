package org.lixl.hadoop.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Administrator on 11/12/2019.
 */
public class Client {

    public static void main(String[] args) throws IOException {
        ClientProtocol proxy = RPC.getProxy(ClientProtocol.class, ClientProtocol.versionID, new InetSocketAddress("127.0.0.1", 8787), new Configuration());
        String result = proxy.echo("123");
        System.out.println(result);
    }
}
