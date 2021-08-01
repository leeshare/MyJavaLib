package org.lixl.opensource.flume.test;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

import java.nio.charset.Charset;

public class MyApp {
    public static void main(String[] args) {
        MyRpcClientFacade client = new MyRpcClientFacade();
        client.init("192.168.123.155", 41414);

        String sampleData = "Hello Flume";
        for(int i = 0; i < 10; i++){
            client.sendDataToFlume(sampleData);
        }
        client.cleanUp();

    }

    public static class MyRpcClientFacade {
        private RpcClient client;
        private String hostname;
        private int port;
        public void init(String hostname, int port) {
            // Setup the RPC connection
            this.hostname = hostname;
            this.port = port;
            //defalut 是  avro
            this.client = RpcClientFactory.getDefaultInstance(hostname, port);
            // Use the following method to create a thrift client (instead of the above line):
            // this.client = RpcClientFactory.getThriftInstance(hostname, port);
        }

        public void sendDataToFlume(String data) {
            // Create a Flume Event object that encapsulates the sample data
            Event event = EventBuilder.withBody(data, Charset.forName("UTF-8"));

            // Send the event
            try {
                client.append(event);
            } catch (EventDeliveryException e) {
                // clean up and recreate the client
                client.close();
                client = null;
                client = RpcClientFactory.getDefaultInstance(hostname, port);
                // Use the following method to create a thrift client (instead of the above line):
                // this.client = RpcClientFactory.getThriftInstance(hostname, port);
            }
        }

        public void cleanUp() {
            // Close the RPC connection
            client.close();
        }
    }
}
