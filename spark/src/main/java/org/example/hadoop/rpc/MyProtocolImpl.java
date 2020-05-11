package org.example.hadoop.rpc;

import org.apache.hadoop.ipc.ProtocolSignature;

import java.io.IOException;

public class MyProtocolImpl implements MyProtocol {

    @Override
    public String echo(String str) throws IOException {
        System.out.println("输入语句为 " + str);
        return str + "hadoop";
    }

    @Override
    public int add(int a, int b) throws IOException {
        System.out.println("result = " + (a + b));
        return a + b;
    }

    @Override
    public long getProtocolVersion(String protocol, long clientVersion) throws IOException {
        return MyProtocol.versionID;
    }

    @Override
    public ProtocolSignature getProtocolSignature(String protocol, long clientVersion, int clientMethodsHash) throws IOException {
        return ProtocolSignature.getProtocolSignature(
                this, protocol, clientVersion, clientMethodsHash);
    }
}
