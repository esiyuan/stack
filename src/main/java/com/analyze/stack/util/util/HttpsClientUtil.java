package com.analyze.stack.util.util;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * http请求客户端
 *
 * @author qusan
 * @create 2019-07-20-18:05
 */
@Slf4j
public class HttpsClientUtil {

    private static CloseableHttpClient httpclient =
            HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.createSystemDefault(), NoopHostnameVerifier.INSTANCE)).build();

    /**
     * @param url
     * @param json
     * @param timeout 超时时间 毫秒
     * @return
     */
    public static String postJson(String url, String json, int timeout) {
        if (log.isDebugEnabled()) {
            log.debug("url:{}  ", url);
        }
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
        httpPost.setHeader("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);
//        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.setEntity(new ByteArrayEntity(json.getBytes(Charsets.UTF_8)));
        httpPost.setConfig(requestConfig);
        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            String result = response.getEntity() == null ? null : EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
            if (log.isDebugEnabled()) {
                log.debug("postJson result = {}", result);
            }
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return result;
            }
            throw new HttpResponseException(response.getStatusLine().getStatusCode(), result);
        } catch (IOException e) {
            log.error("postJson 请求异常: url = {} 入参 = {}", url, json, e);
            throw new RuntimeException(e);
        }
    }

    public static String get(String url, int timeout) {
        log.info("请求url: {}", url);
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
        httpGet.setConfig(requestConfig);
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            String result = response.getEntity() == null ? null : EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return result;
            }
            throw new HttpResponseException(response.getStatusLine().getStatusCode(), "url:" + url);
        } catch (IOException e) {
            log.error("get 请求异常: url = {} ", url, e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String s = get("http://10.200.8.220/api/playlist/vrs/new_upload/2020-06-30/3243729%E5%A4%A9%E5%A4%A9%E5%90%91%E4%B8%8A20121102%E6%9C%9F%E8%90%BD%E9%A9%AC%E5%AE%98%E5%91%98%E6%89%93%E7%A0%81%E6%B5%8B%E8%AF%95.ts", 2000);
        System.out.println(s);
    }


    public static byte[] downLoad(String url, int timeout) {
        if (log.isDebugEnabled()) {
            log.debug("url:{}  ", url);
        }
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
        httpGet.setConfig(requestConfig);
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return response.getEntity() == null ? null : EntityUtils.toByteArray(response.getEntity());
            }
            throw new HttpResponseException(response.getStatusLine().getStatusCode(), "url:" + url);
        } catch (IOException e) {
            log.error("downLoad 请求异常: url = {} ", url, e);
            throw new RuntimeException(e);
        }
    }
//
//    public static void main(String[] args) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "abc");
//        System.out.println(postJson("http://localhost:8080/post", JSON.toJSONString(map), 1000));
//        ;
//    }

}
