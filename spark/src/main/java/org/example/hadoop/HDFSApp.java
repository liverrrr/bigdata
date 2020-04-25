package org.example.hadoop;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import java.io.IOException;
import java.net.URI;


public class HDFSApp {
    private final static String HDFSURL="hdfs://hadoop001:9000/";
    private static Configuration configuration;
    private static FileSystem fileSystem;

    public static void init() throws Exception {
        configuration = new Configuration();
        configuration.set("dfs.replication","1");
        configuration.set("dfs.client.use.datanode.hostname","true");
        fileSystem = FileSystem.get(new URI(HDFSURL), configuration, "hadoop");
    }

    public static void rename(String time) throws IOException {
        if (StringUtils.isEmpty(time)){
            System.out.println("请检查输入参数是否有值");
        }
        Path path=new Path("/ruozedata/"+time);
        if (!fileSystem.exists(path)){
            System.out.println("请检查hdfs上是否有该路径");
        }
        RemoteIterator<LocatedFileStatus> remoteIterator = fileSystem.listFiles(path, true);
        int flag=1;
        while(remoteIterator.hasNext()){
            LocatedFileStatus fileStatus = remoteIterator.next();
            if (!fileStatus.isDirectory()){
                Path srcPath = fileStatus.getPath();
                String[] split = srcPath.getName().split("\\.");
                String dstName = flag+"-"+time+split[1];
                String dstParent = srcPath.getParent().toString();
                Path dstPath = new Path(dstParent+"/"+dstName);
                fileSystem.rename(srcPath,dstPath);
                flag++;
            }
        }
        Path newPath = new Path("/ruozedata/", time.substring(2));
        fileSystem.rename(path,newPath);
    }

    public static void tearDown() throws IOException {
        fileSystem.close();
    }


    public static void main(String[] args) throws Exception {
        String time = "20190826";
        init();
        rename(time);
        tearDown();
    }
}
