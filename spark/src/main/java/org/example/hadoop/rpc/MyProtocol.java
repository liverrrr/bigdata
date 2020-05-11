package org.example.hadoop.rpc;

import org.apache.hadoop.ipc.VersionedProtocol;

import java.io.IOException;

public interface MyProtocol extends VersionedProtocol {
    public static final long versionID = 1L;

    public String echo(String str) throws IOException;

    public int add(int a, int b) throws IOException;
}
