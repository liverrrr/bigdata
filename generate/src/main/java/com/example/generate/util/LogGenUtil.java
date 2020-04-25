package com.example.generate.util;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 域名  ruozedata.com
 * 时间  [09/01/2019 00:01:02 +0800]
 * 流量  4090     一部分日志是正确（数值类型）  一部分日志是错误的（字符串类型）
 * ip    x.x.x.x
 */
public class LogGenUtil {

    public static void main(String[] args) throws Exception {
        Random random = new Random();
        String outputPath = "D:\\tmp\\random.txt";
        StringBuffer result = new StringBuffer();
        for (int i = 1; i < 100; i++) {
            String dateTime = dateTimeGen("2019-09-01 00:03:01", i);
            String flow = flowGen(random, i);
            String ip = random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256);
            System.out.println("ruozedata.com" + "\t" + dateTime + "\t" + flow + "\t" + ip);
            result.append("ruozedata.com").append("\t")
                    .append(dateTime).append("\t")
                    .append(flow).append("\t")
                    .append(ip).append("\r\n");
        }

        File file = new File(outputPath);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(result.toString().getBytes());
    }

    private static String flowGen(Random random, int i) {
        String flow;
        if (i % 5 == 0) {
            char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
            flow = letters[random.nextInt(letters.length)] + "" + letters[random.nextInt(letters.length)] + "" + letters[random.nextInt(letters.length)];
        } else {
            flow = random.nextInt(10000) + "";
        }
        return flow;
    }

    private static String dateTimeGen(String start, int i) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime time = startDateTime.plusDays(i).plusHours(i).plusMinutes(i * 15).plusSeconds(i * 25);
        dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return "[" + time.format(dateTimeFormatter) + " +0800]";
    }


}
