/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alvin.api.application;

import org.alvin.api.utils.HttpClientV4_3Utils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tangzhichao
 */
public class StringTest {

    public static void main(String[] args) {
        String text = "{\"username\":\"xx\",\"info\":{\"uuid\":\"ed0f0273-0b09-4c15-9d9b-4bd06ff17f7b\",\"displayName\":\"yw172f33021w\",\"vcpus\":\"2\",\"memoryMb\":4096,\"instanceTypeId\":1,\"imageId\":\"2\",\"osId\":26,\"clientUuid\":\"UNKNOWN\",\"host\":{\"sId\":65459,\"uuid\":\"930138c5-af77-4c40-9869-0967df8f8f2c\"},\"vdisks\":[{\"vdiskOrder\":1,\"sizeMb\":40960,\"uuid\":\"6759c417e67eb68be52e42bd8478b031\"},{\"vdiskOrder\":2,\"sizeMb\":102400,\"uuid\":\"9ce320e824574309080bfa5c236999ba\"}],\"vifs\":[{\"ip\":\"150.242.59.82\",\"mac\":\"02:00:96:f2:3b:52\",\"type\":\"vnet\"},{\"ip\":\"10.50.0.7\",\"mac\":\"02:00:0a:32:00:07\",\"type\":\"vnet\"}]}}";
        String action = "create";
        String url = "http://localhost:1878/serverinterface/vmcluster/api";
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        new NameValuePair
        
        Map<String, String> params = new HashMap<>();
        params.put("action", action);
        params.put("info", text);
        CloseableHttpClient client;
        try {
            client = HttpClientV4_3Utils.createSimpleClient();
            CloseableHttpResponse response = HttpClientV4_3Utils.doGet(url, params, client);
            String content = HttpClientV4_3Utils.getContent(response);
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String text = "[北京航天控制仪器研究所,北京航天控制仪器研究所,北京劲道有限公司,北京航天控制仪器研究所,北京航天控制仪器研究所,北京劲道有限公司,北京劲道有限公司,北京航天控制仪器研究所]";
//        String regex = "([^,\\[\\]]+)";
//        Pattern p = Pattern.compile(regex);
//        Matcher m = p.matcher(text);
//        List<String> list = new ArrayList<String>();
//        while (m.find()) {
//            if (!list.contains(m.group(1))) {
//                list.add(m.group(1));
//            }
////            System.err.println(m.group(1));
//        }
//        System.out.println(list);
    }

}
