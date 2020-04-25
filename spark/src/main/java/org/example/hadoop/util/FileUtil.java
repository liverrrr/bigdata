package org.example.hadoop.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

public class FileUtil {
    private final static String HDFSURL = "hdfs://hadoop001:8020/";

    public static void deleteTarget(String path) throws Exception {
        Configuration config = new Configuration();
        config.set("dfs.replication", "1");
        config.set("dfs.client.use.datanode.hostname", "true");
        FileSystem fileSystem = FileSystem.get(new URI(HDFSURL), config, "hadoop");

        Path targetPath = new Path(path);
        if (fileSystem.exists(targetPath)) {
            fileSystem.delete(targetPath, true);
        }
        fileSystem.close();
    }
}
