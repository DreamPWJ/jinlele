package com.jinlele.util.weixinUtils.text;


import com.jinlele.util.weixinUtils.util.Parameter;
import com.jinlele.util.weixinUtils.vo.Token;
import com.jinlele.util.weixinUtils.vo.WeiXinUtil;
import net.sf.json.JSONObject;

/**
 *
 */
public class TestToken {


    public static void main(String[] args) {
        //第三方用户唯一凭证
       // String appId = "wx0cf227cc0e526f1c";
        //测试号的
        String appId = Parameter.corId;

        //第三方用户唯一凭证密钥
        //String appSecret = "3cbf9b1169dsf5c56566b939ead5104fd";
        //测试号的
        String appSecret = Parameter.appsecret;

        //调用接口获取凭证
        Token token = WeiXinUtil.getToken(appId, appSecret);
        System.out.println("access_token=="+token.getAccessToken());
        System.out.println("express_in=="+token.getExpiresIn());


        //获取微信服务期Ip地址
        System.out.println("\n=======获取获取微信服务期Ip地址=======");
        String ipUrl = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";
        ipUrl=ipUrl.replace("ACCESS_TOKEN",token.getAccessToken());
        System.out.println("ipUrl=="+ipUrl);
        JSONObject servers = WeiXinUtil.httpsRequset(ipUrl , "GET" ,null);
        System.out.println(servers.toString());
       /* {
            "ip_list": [
            "127.0.0.1",
                    "127.0.0.2",
                    "101.226.103.0/25"
            ]
        }*/


    }

}
