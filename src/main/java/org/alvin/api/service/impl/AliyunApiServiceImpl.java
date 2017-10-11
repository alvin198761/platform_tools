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

@ClusterService(ClusterService.TYPE_ALIYUN)
public class AliyunApiServiceImpl implements IPlatFormApiService {

    private final String url = "https://help.aliyun.com/product/8314827_25365.html";
    private final String publicParams = "/**	否	返回值的类型，支持 JSON 与 XML。默认为 XML。	*/	\n"
            + "String	Format	=\"\";\n"
            + "/**	是	API 版本号，为日期形式：YYYY-MM-DD，本版本对应为 2014-05-26。	*/	\n"
            + "String	Version	=\"\";\n"
            + "/**	是	阿里云颁发给用户的访问服务所用的密钥 ID。	*/	\n"
            + "String	AccessKeyId	=\"\";\n"
            + "/**	是	签名结果串，关于签名的计算方法，请参见<签名机制>。	*/	\n"
            + "String	Signature	=\"\";\n"
            + "/**	是	签名方式，目前支持 HMAC-SHA1。	*/	\n"
            + "String	SignatureMethod	=\"\";\n"
            + "/**	是	请求的时间戳。日期格式按照 ISO8601 标准表示，并需要使用 UTC 时间。格式为： 	*/	\n"
            + "/**		YYYY-MM-DDThh:mm:ssZ 	*/		 \n"
            + "/**		例如，2014-05-26T12:00:00Z（为北京时间 2014 年 5 月 26 日 20 点 0 分 0 秒）。	*/		 \n"
            + "String	Timestamp	=\"\";\n"
            + "/**	是	签名算法版本，目前版本是 1.0。	*/	\n"
            + "String	SignatureVersion	=\"\";\n"
            + "/**	是	唯一随机数，用于防止网络重放攻击。用户在不同请求间要使用不同的随机数值	*/	\n"
            + "String	SignatureNonce	=\"\";\n"
            + "/**	否	本次 API 请求访问到的资源拥有者账户，即登录用户名。 */		\n"
            + "/**		此参数的使用方法，详见< 借助 RAM 实现子账号对主账号的 ECS 资源访问 >，（只能在 RAM 中可对 ECS 资源进行授权的 Action 中才能使用此参数，否则访问会被拒绝）		*/\n"
            + "	String	ResourceOwnerAccount	=\"\";\n";

    public final String[] publicParamsName = {"Format",
        "Version",
        "AccessKeyId",
        "Signature",
        "SignatureMethod",
        "Timestamp",
        "SignatureVersion",
        "SignatureNonce",
        "ResourceOwnerAccount"};

    @Override
    public Map<String, String> getRequestParams(PlatTypeItem item) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        String content = HttpClientV4_3Utils.getSimpleGet(url);
        // System.out.println(content);
        Document doc = Jsoup.parse(content);
        Element rootNode = doc.getElementsByClass("menu").first();
        rootNode.getElementsByTag("li").stream().filter((el) -> {
            return el.className().equals("level1");
        }).forEach((e) -> {
            Elements titleTag = e.getElementsByTag("a");
            if (titleTag.attr("title").equals("API 参考")) {
                String href = e.getElementsByTag("a").stream().filter((subE) -> {
                    return subE.attr("title").equals(item.getSearchText());
                }).findFirst().get().attr("href");
                sb.append(href);
                return;
            }

        });
        if (sb.length() == 0) {
            return null;
        }
        // Thread.sleep(2000);
        String detailUrl = "https://help.aliyun.com" + sb.toString();
        content = HttpClientV4_3Utils.getSimpleGet(detailUrl.trim());
        sb.delete(0, sb.length());
        // System.out.println(content);
        doc = Jsoup.parse(content);
        doc.getElementsByTag("table").stream().filter((e) -> {
            return e.className().equals("ecs ecs_interface_request");
        }).findFirst().get().getElementsByTag("tr").stream().skip(1).forEach((e) -> {
            Elements tds = e.getElementsByTag("td");
            String key = tds.get(0).text();
            String type = tds.get(1).text();
            String isNullDesc = tds.get(2).text();
            String commentText = tds.get(3).text();
            String actionName = "";
            if (key.equals("Action")) {
                actionName = commentText.substring(commentText.lastIndexOf("：") + 1).trim();
            }
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
                    sb.append("\"").append(actionName).append("\";");
                }
            }
            sb.append(System.lineSeparator());
        }
        );
        resultMap.put(
                "request", sb.toString());
        //
        sb.delete(
                0, sb.length());

        return resultMap;
    }

    @Override
    public String getPublicParmas() {
        return publicParams;
    }

}
