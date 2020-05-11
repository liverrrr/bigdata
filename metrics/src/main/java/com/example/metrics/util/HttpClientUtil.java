package com.example.metrics.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;

public class HttpClientUtil {

    public static String getContent(String targetUri,String key,String value){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        URI uri = null;
        String content = null;
        try {
            if (StringUtils.isBlank(key) && StringUtils.isBlank(value)){
                uri = new URI(targetUri);
            } else {
                uri = new URIBuilder(targetUri).setParameter(key, value).build();
            }
            HttpGet httpGet = new HttpGet(uri);
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (httpResponse != null){
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public static void main(String[] args) {
        String content = getContent("http://hadoop001:50070/jmx", "qry", "Hadoop:service=NameNode,name=NameNodeInfo");
        System.out.println(content);
    }

}
