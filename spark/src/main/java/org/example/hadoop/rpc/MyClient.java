package org.example.hadoop.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MyClient {

    public static void main(String[] args) throws IOException {
        MyProtocol proxy = RPC.getProxy(MyProtocol.class, MyProtocol.versionID,
                new InetSocketAddress("localhost", 9876), new Configuration());
        int result = proxy.add(4, 5);
        System.out.println(result);
        String s = proxy.echo("hello world");
        System.out.println(s);
    }

}
