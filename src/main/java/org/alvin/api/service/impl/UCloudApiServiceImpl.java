package org.alvin.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.alvin.api.service.IPlatFormApiService;
import org.alvin.api.ui.renderer.PlatTypeItem;
import org.alvin.api.utils.ClusterService;
import org.alvin.api.utils.HttpClientV4_3Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@ClusterService(ClusterService.TYPE_UCLOUD)
public class UCloudApiServiceImpl implements IPlatFormApiService {

    private String publicParams = "/**	对应的API名称，如CreateUHostInstance	TRUE	*/	\n"
            + "String	Action	=\"\";\n"
            + "/**	用户公钥	TRUE	*/	\n"
            + "String	PublicKey	=\"\";\n"
            + "/**	根据公钥及API指令生成的用户签名，参见 签名算法	TRUE	*/	\n"
            + "String	Signature	=\"\";\n"
            + "/**	项目ID，为空时及为默认项目	FALSE	*/	\n"
            + "String	ProjectId	=\"\";";

    @Override
    public Map<String, String> getRequestParams(PlatTypeItem item) throws Exception {
        Map<String, String> resultMap = findByUHost(item);
        if (resultMap != null) {
            return resultMap;
        }
        resultMap = findByUpHost(item);
        return resultMap;
    }

    private Map<String, String> findByUpHost(PlatTypeItem item) throws Exception {
        String url = "https://docs.ucloud.cn/api-docs/uphost-api/index.html";
        return getByUrl(url, "uphost", item);
    }

    private Map<String, String> findByUHost(PlatTypeItem item) throws Exception {
        String url = "https://docs.ucloud.cn/api-docs/uhost-api/index.html";
        return getByUrl(url, "uhost", item);
    }

    private Map<String, String> getByUrl(String url, String id, PlatTypeItem item) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        String content = HttpClientV4_3Utils.getSimpleGet(url);
        // System.out.println(content);
        Document doc = Jsoup.parse(content);
        String detailURL = "https://docs.ucloud.cn/api-docs/" + id + "-api/";
        Elements els = doc.getElementById(id).child(1).child(0).getElementsByTag("a");
        String actionName = "";
        for (Element el : els) {
            String title = el.text();
            String href = el.attr("href");
            if (!title.startsWith(item.getSearchText() + " -")) {
                continue;
            }
            sb.append(href);
            actionName = title;
            break;
        }
        if (sb.length() == 0) {
            return null;
        }

        detailURL = detailURL + sb.toString();
        // System.out.println(detailUrl);
        content = HttpClientV4_3Utils.getSimpleGet(detailURL.trim());
        sb.delete(0, sb.length());
//        System.out.println(content);
        doc = Jsoup.parse(content);
        Elements trEls = doc.getElementsByTag("table").first().getElementsByTag("tr");
        sb.append("/**").append(System.lineSeparator());
        sb.append(actionName).append(System.lineSeparator());
        sb.append("*/").append(System.lineSeparator());
        trEls.subList(1, trEls.size() - 1).stream().forEach((e) -> {
            Elements tds = e.getElementsByTag("td");
            String key = tds.get(0).text();
            String type = tds.get(1).text();
            String commentText = tds.get(2).text();
            String isNullDesc = tds.get(3).text();
            if (item.isJavaFiled()) {
                char c = key.charAt(0);
                key = Character.toLowerCase(c) + key.substring(1);
            }
            if (item.isToStringVar()) {
                key = "\"" + key + "\"";
            }
            //
            if (type == null || type.trim().isEmpty()) {
                type = "NotFound>String";
            }
            if (item.isComment() || item.isHasNull()) {
                sb.append("/**").append(System.lineSeparator());
            }
            if (item.isComment()) {
                sb.append(commentText).append(System.lineSeparator());
            }
            if (item.isHasNull()) {
                sb.append("是否必须参数：").append(isNullDesc).append(System.lineSeparator());
            }
            if (item.isComment() || item.isHasNull()) {
                sb.append("*/").append(System.lineSeparator());
            }
            if (item.isPrivateKeyWork()) {
                sb.append("private ");
            }
            if (item.isOnlyName()) {
                sb.append(key);
            } else {
                sb.append(type).append(" ").append(key);
                sb.append(" = ");
                if (type.equals("Integer")) {
                    sb.append("0;");
                } else {
                    sb.append("\"\";");
                }
            }
            sb.append(System.lineSeparator());
        });
        resultMap.put("request", sb.toString());
//        //
        sb.delete(0, sb.length());
        return resultMap;
    }

    @Override
    public String getPublicParmas() {
        return publicParams;
    }

}
