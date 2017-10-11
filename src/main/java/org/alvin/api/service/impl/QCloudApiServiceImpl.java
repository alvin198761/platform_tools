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

@ClusterService(ClusterService.TYPE_QCLOUD)
public class QCloudApiServiceImpl implements IPlatFormApiService {

    private String publicParams = "/**	接口指令的名称，例如: DescribeInstances	是	*/	\n"
            + "String	Action	=\"\";\n"
            + "/**	区域参数，用来标识希望操作哪个区域的实例。可选: 	是	*/	\n"
            + "/**	bj:北京		*/			 \n"
            + "/**	gz:广州		*/			 \n"
            + "/**	sh:上海		*/			 \n"
            + "/**	hk:香港		*/			 \n"
            + "/**	ca:北美		*/			 \n"
            + "String	Region	=\"\";\n"
            + "/**	当前UNIX时间戳	是	*/	\n"
            + "Long	Timestamp	=0;\n"
            + "/**	随机正整数，与 Timestamp 联合起来, 用于防止重放攻击	是	*/	\n"
            + "Long	Nonce	=0;\n"
            + "/**	由腾讯云平台上申请的标识身份的 SecretId 和 SecretKey, 其中 SecretKey 会用来生成 Signature	是	*/	\n"
            + "/**	具体参考 接口鉴权 页面		*/			\n"
            + "String	SecretId	=\"\";\n"
            + "/**	请求签名，用来验证此次请求的合法性, 	是	*/	\n"
            + "/**	具体参考 接口鉴权 页面		*/\n"
            + "String	Signature	=\"\";";
    private final String url = "https://www.qcloud.com/doc/api/229/568";

    @Override
    public Map<String, String> getRequestParams(PlatTypeItem item) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        String content = HttpClientV4_3Utils.getSimpleGet(url);
        // System.out.println(content);
        Document doc = Jsoup.parse(content);
        Element rootNode = doc.getElementsByClass("table-box").first();
        rootNode = rootNode.child(0);//.child(0);
        rootNode.getElementsByClass("side-nav-inner-list").stream().filter((e) -> {
            return e.tagName().equals("div");
        }).skip(6).forEach((e) -> {
            e.getElementsByTag("ul").forEach((ule) -> {
                ule.getElementsByTag("a").stream().forEach((ae) -> {
                    if (ae.attr("title").equals(item.getSearchText())) {
                        sb.append(ae.attr("href"));
                        return;
                    }
                });
            });
        });;
        if (sb.length() == 0) {
            return null;
        }
        // Thread.sleep(2000);
        String detailUrl = "https://www.qcloud.com" + sb.toString();
        // System.out.println(detailUrl);
        content = HttpClientV4_3Utils.getSimpleGet(detailUrl.trim());
        sb.delete(0, sb.length());
        // System.out.println(content);
        doc = Jsoup.parse(content);
        Elements trEls = doc.getElementsByTag("table").first().getElementsByTag("tr");
        String pHtml = doc.getElementById("docArticleContent").getElementsByTag("p").first().html();
        sb.append("/**").append(System.lineSeparator());
        sb.append(pHtml.replaceFirst("[<]br[>]", System.lineSeparator())).append(System.lineSeparator());
        sb.append("*/").append(System.lineSeparator());
        trEls.stream().skip(1).forEach((e) -> {
            Elements tds = e.getElementsByTag("td");
            String key = tds.get(0).text();
            String isNullDesc = tds.get(1).text();
            String type = tds.get(2).text();
            String commentText = tds.get(3).text();

            if (!item.isOnlyName()) {
                key = key.replaceAll("[^\\w]", "_");
            }
            if (item.isJavaFiled()) {
                char c = key.charAt(0);
                key = Character.toLowerCase(c) + key.substring(1);
            }
            if (item.isToStringVar()) {
                key = "\"" + key + "\"";
            }
            if (type == null || type.trim().isEmpty()) {
                type = "NotFound>String";
            } else if (type.equals("Int")) {
                type = "Integer";
            } else if (type.equals("UInt")) {
                type = "Long";
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
  if(item.isPrivateKeyWork()){
                sb.append("private ");
            }
            if (item.isOnlyName()) {
                sb.append(key);
            } else {
                sb.append(type).append(" ").append(key);

                String actionName = "";
                if (key.equals("Action")) {
                    actionName = commentText.substring(commentText.lastIndexOf("：") + 1).trim();
                }
                sb.append(" = ");
                if (type.equals("Integer") || type.equals("Long")) {
                    sb.append("0;");
                } else {
                    sb.append("\"").append(actionName).append("\";");
                }
            }
            sb.append(System.lineSeparator());
        });
        resultMap.put("request", sb.toString());
        //
        sb.delete(0, sb.length());

        return resultMap;
    }

    @Override
    public String getPublicParmas() {
        return publicParams;
    }

}
