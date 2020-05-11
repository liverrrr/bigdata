package org.example.hadoop.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

public class MyServer {

    public static void main(String[] args) throws IOException {
        RPC.Server server = new RPC.Builder(new Configuration())
                .setProtocol(MyProtocol.class)
                .setInstance(new MyProtocolImpl())
                .setBindAddress("localhost")
                .setPort(9876)
                .setNumHandlers(10)
                .build();
        server.start();
    }

}
