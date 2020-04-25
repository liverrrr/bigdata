package com.example.generate.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtil {

    static void doPost(String hostname, String log) {
        String path = hostname + "/generate";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);//允许写出
            connection.setDoInput(true);//允许读入
            connection.setUseCaches(false);//不使用缓存
            connection.setConnectTimeout(30000); //30秒连接超时
            connection.setReadTimeout(30000);    //30秒读取超时
//            connection.connect();
//            OutputStream stream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
            writer.write(log);
            writer.flush();
            writer.close();

            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
