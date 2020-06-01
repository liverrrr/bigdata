package com.example.generate.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.DigestUtils;

import java.security.InvalidParameterException;
import java.util.Random;

public class MockActionLog extends Thread {

    private Random random = new Random();
    private JSONObject info;
    private String uri;
    private String action;

    public MockActionLog(JSONObject info, String uri, String action) {
        this.info = info;
        this.uri = uri;
        this.action = action;
    }

    /**
     * 用户行为日志基础信息：appId、accountId、accountName、age、gender、time
     */
    private void baseInfoGen() {
        if (info == null || !info.isEmpty()) {
            throw new InvalidParameterException("请检查参数内容是否清空或者是否为null");
        }
        int uid = random.nextInt(10000);
        String phone = phoneGen();
        String nickname = "rdp" + uid;
        long time = System.currentTimeMillis() / 1000;
        info.put("uid", uid);
        info.put("nickname", nickname);
        info.put("phone", phone);
        info.put("time", time);
        info.put("clubId", "19191919");
    }

    /**
     * 登陆日志专属信息：ip、deviceCode、version、device、platform
     */
    private void loginInfoGen() {
        if (info == null) {
            throw new InvalidParameterException("请检查login日志内容个人基础信息是否为null");
        }
        String ip = random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256);
        String device = deviceGen(random);
        info.put("ip", ip);
        info.put("deviceCode", DigestUtils.md5DigestAsHex(("rdp-poker" + random.nextInt(300) + System.currentTimeMillis()).getBytes()));
        info.put("appVersion", appVersionGen(random));
        info.put("device", device);
        info.put("os", "Apple".equals(device) ? "IOS" : "Android");
        info.put("network", "");
        info.put("channel", "ps");
        info.put("manufacturer", "");
        info.put("browser", "");
        info.put("osVersion", "");
        info.put("gps", "");
    }

    private void onlineInfoGen() {
        info.put("online", random.nextInt(100));
        info.put("inGame", random.nextInt(100));
        info.put("inHall", random.nextInt(100));
        info.put("inMTT", random.nextInt(100));
        info.put("inPrivate", random.nextInt(100));
        info.put("inClubRoom", random.nextInt(100));
        info.put("inSNG", random.nextInt(100));
        info.put("time", System.currentTimeMillis() / 1000);
    }


    private String phoneGen() {
        String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + third;
    }

    private static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    private String appVersionGen(Random random) {
        String[] versions = {"1.2.0", "1.2.1", "1.2.2", "1.3.0"};
        return versions[random.nextInt(versions.length)];
    }

    private String deviceGen(Random random) {
        String[] devices = {"Apple", "OnePlus+", "MI", "HUAWEI", "OPPO"};
        return devices[random.nextInt(devices.length)];
    }

    public void clean() {
        info.clear();
    }

    public JSONObject getInfo() {
        return info;
    }

    @Override
    public void run() {
        while (true) {
            if (StringUtils.equalsIgnoreCase(action, "Login")) {
                baseInfoGen();
                loginInfoGen();
            } else {
                onlineInfoGen();
            }
            String content = info.toJSONString();
            String time = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
            HttpUtil.doPost(uri, time + "[Log]#PBJ#[" + action + "]" + content);
            clean();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String uri = "http://hadoop008:7777";
        for (int i = 0; i < 10; i++) {
            JSONObject object = new JSONObject();
            new MockActionLog(object, uri, "Login").start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new MockActionLog(object, uri, "online").start();
        }
    }
}
