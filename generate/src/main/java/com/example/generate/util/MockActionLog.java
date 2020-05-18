package com.example.generate.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.DigestUtils;

import java.security.InvalidParameterException;
import java.util.Random;

public class MockActionLog extends Thread{

    private Random random = new Random();
    private JSONObject info;
    private String uri;

    public MockActionLog(JSONObject info,String uri) {
        this.info = info;
        this.uri = uri;
    }

    /**
     * 用户行为日志基础信息：appId、accountId、accountName、age、gender、time
     */
    private void baseInfoGen() {
        if (info == null || !info.isEmpty()) {
            throw new InvalidParameterException("请检查参数内容是否清空或者是否为null");
        }
        int accountId = random.nextInt(10000);
        int age = accountId % 120;
        String accountName = "rdp" + accountId;
        long time = System.currentTimeMillis();
        info.put("appId", DigestUtils.md5DigestAsHex("rdp-poker-12.3".getBytes()));
        info.put("accountId", accountId);
        info.put("accountName", accountName);
        info.put("age",age);
        info.put("gender",age % 2 ==0 ? "男" : "女");
        info.put("time", time);
    }

    /**
     * 登陆日志专属信息：ip、deviceCode、version、device、platform
     */
    private void loginInfoGen(){
        if (info == null){
            throw new InvalidParameterException("请检查参数内容是否为null");
        }
        String ip = random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256);
        String device = deviceGen(random);
        info.put("action","login");
        info.put("ip",ip);
        info.put("deviceCode",DigestUtils.md5DigestAsHex(("rdp-poker" + random.nextInt(300) + System.currentTimeMillis()).getBytes()));
        info.put("version",versionGen(random));
        info.put("device",device);
        info.put("platform", "Apple".equals(device) ? "IOS" : "Android");
    }

    private String versionGen(Random random) {
        String[] versions = {"1.2.0", "1.2.1", "1.2.2", "1.3.0"};
        return versions[random.nextInt(versions.length)];
    }

    private String deviceGen(Random random) {
        String[] devices = {"Apple", "OnePlus+", "MI", "HUAWEI","OPPO"};
        return devices[random.nextInt(devices.length)];
    }

    public void clean(){
        info.clear();
    }

    public JSONObject getInfo() {
        return info;
    }

    @Override
    public void run() {
        while (true) {
            baseInfoGen();
            loginInfoGen();
            String content = info.toJSONString();
            HttpUtil.doPost(uri, content);
            clean();
        }
    }

    public static void main(String[] args) {
        String uri = "http://localhost:7777";
        for (int i=0;i<100;i++){
            JSONObject object = new JSONObject();
            new MockActionLog(object,uri).start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
