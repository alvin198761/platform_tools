package org.alvin.api.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientV4_3Utils {

    /**
     * get 请求
     *
     * @param url
     * @param params
     * @param client
     * @return
     * @throws Exception
     */
    public static CloseableHttpResponse doGet(String url, Map<String, String> params, CloseableHttpClient client)
            throws Exception {
        String requestPrama = "";
        if (params != null) {
            for (Entry<String, String> entry : params.entrySet()) {
                if (requestPrama.isEmpty()) {
                    requestPrama += "?";
                } else {
                    requestPrama += "&";
                }
                requestPrama += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "utf-8");
            }
        }
        HttpGet get = new HttpGet(url + requestPrama);
        return client.execute(get);
    }

    /**
     * post 请求
     *
     * @param url
     * @param params
     * @param client
     * @return
     * @throws Exception
     */
    public static CloseableHttpResponse doPost(String url, List<NameValuePair> params, CloseableHttpClient client)
            throws Exception {
        HttpPost post = new HttpPost(url);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
        post.setEntity(entity);
        return client.execute(post);
    }

    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
    /**
     * 提交json 数据
     * @param url
     * @param params
     * @param client
     * @return
     * @throws Exception 
     */
    public static CloseableHttpResponse doPostJsonBody(String url, String params, CloseableHttpClient client) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
//        String encoderJson = URLEncoder.encode(params, "utf-8");
        StringEntity se = new StringEntity(params);
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
        httpPost.setEntity(se);
        return client.execute(httpPost);
    }

    /**
     * 获取文本，并关闭一个请求
     *
     * @param response
     * @return
     * @throws Exception
     */
    public static String getContent(CloseableHttpResponse response) throws Exception {
        String content = EntityUtils.toString(response.getEntity(), "utf-8");
        EntityUtils.consume(response.getEntity());
        return content;
    }

    /**
     * 普通客户端
     *
     * @return
     */
    public static CloseableHttpClient createSimpleClient() {
        RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build();
        return client;
    }

    public static void main(String[] args) throws Exception {
        String url ="http://localhost:1878/serverinterface/vmcluster/api?action=createOrUpdate";
        String params ="{\"username\":\"xx\",\"info\":{\"uuid\":\"ed0f0273-0b09-4c15-9d9b-4bd06ff17f7b\",\"displayName\":\"yw172f33021w\",\"vcpus\":\"2\",\"memoryMb\":4096,\"instanceTypeId\":1,\"imageId\":\"2\",\"osId\":26,\"clientUuid\":\"UNKNOWN\",\"host\":{\"sId\":65459,\"uuid\":\"930138c5-af77-4c40-9869-0967df8f8f2c\"},\"vdisks\":[{\"vdiskOrder\":1,\"sizeMb\":40960,\"uuid\":\"6759c417e67eb68be52e42bd8478b031\"},{\"vdiskOrder\":2,\"sizeMb\":102400,\"uuid\":\"9ce320e824574309080bfa5c236999ba\"}],\"vifs\":[{\"ip\":\"150.242.59.82\",\"mac\":\"02:00:96:f2:3b:52\",\"type\":\"vnet\"},{\"ip\":\"10.50.0.7\",\"mac\":\"02:00:0a:32:00:07\",\"type\":\"vnet\"}],\"deviceTypeId\":11}}";
        CloseableHttpClient client = createSimpleClient();
        CloseableHttpResponse response = doPostJsonBody(url, params, client);
        String content = getContent(response);
        System.out.println(content);
    }

    public static String getSimpleGet(String url) throws Exception {
        RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        try (CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            CloseableHttpResponse response = HttpClientV4_3Utils.doGet(url, null, client);
            return getContent(response);
        }
    }

}
