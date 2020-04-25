package org.example.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class ZookeeperCuratorApp {
    private static Logger logger = LoggerFactory.getLogger(ZookeeperCuratorApp.class);
    private static CuratorFramework client = null;
    private static CountDownLatch connected = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        init();
        System.out.println("客户端状态：" + client.getState());
        String path = "/ruozedata";
        ZnodeWatcher(path);
        clean();
    }

    private static void ZnodeWatcher(String path) throws Exception {
        TreeCache treeCache = new TreeCache(client, path);
        TreeCacheListener listener = (curatorFramework, treeCacheEvent) -> {
            ChildData data = treeCacheEvent.getData();
            if (data == null) {
                logger.warn("节点不存在");
            }
            switch (treeCacheEvent.getType()) {
                case NODE_ADDED:
                    logger.warn("节点增加,path={},data={},stat={}", data.getPath(), new String(data.getData())
                            , "[version:" + data.getStat().getVersion() + "\t" + "ephemeralOwner:" + data.getStat().getEphemeralOwner() + "\t" + "numberChildren:" + data.getStat().getNumChildren() + "]");
                    break;
                case NODE_UPDATED:
                    logger.warn("节点修改,path={},data={},stat={}", data.getPath(), new String(data.getData())
                            , "[version:" + data.getStat().getVersion() + "\t" + "ephemeralOwner:" + data.getStat().getEphemeralOwner() + "\t" + "numberChildren:" + data.getStat().getNumChildren() + "]");
                    break;
                case NODE_REMOVED:
                    logger.warn("节点删除,path={},data={},stat={}", data.getPath(), new String(data.getData())
                            , "[version:" + data.getStat().getVersion() + "\t" + "ephemeralOwner:" + data.getStat().getEphemeralOwner() + "\t" + "numberChildren:" + data.getStat().getNumChildren() + "]");
                    break;
                default:
                    break;
            }
        };
        treeCache.getListenable().addListener(listener);
        treeCache.start();
        connected.await();
    }

    //删除节点（以及子节点）
    private static void deleteZnode() throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath("/ruozedata");
        //强制删除，在网络不好情况下可以使用
        client.delete().guaranteed().forPath("/tmp");
    }

    //得到节点数据
    private static void getZoneData() throws Exception {
        byte[] bytes = client.getData().forPath("/ruozedata/hadoop");
        System.out.println(new String(bytes));
    }


    //修改节点数据
    private static void setZnodeData() throws Exception {
        //默认最新版本
        Stat stat = client.setData().forPath("/ruozedata/hadoop", "abcd".getBytes());
        //传入版本号，很有可能报错
//        Stat stat = client.setData().withVersion(1).forPath("/ruozedata/hadoop", "abcd".getBytes());
        System.out.println("version= " + stat.getVersion() + "\t" + "ephemeralOwner= " + stat.getEphemeralOwner() + "\t" + "numberChildren= " + stat.getNumChildren());
    }

    //递归创建永久节点
    private static void createZnode() throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/ruozedata/hadoop", "123".getBytes());
    }

    private static void clean() {
        client.close();
    }

    private static void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        client = CuratorFrameworkFactory.newClient("hadoop001:2181", 5000, 1000, retryPolicy);
        client = CuratorFrameworkFactory.builder()
                .connectString("hadoop001:2181")
                .sessionTimeoutMs(10000)
                .connectionTimeoutMs(1000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }
}
