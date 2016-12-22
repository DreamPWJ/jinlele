package com.jinlele.util.weixinUtils.vo;


import com.jinlele.util.weixinUtils.util.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定义线程定时获取微信access_token
 */
public class TokenThread implements Runnable {

    private static Logger log = LoggerFactory.getLogger(TokenThread.class);
    //第三方用户唯一凭证
    public  static  String appid = Parameter.corId;
    //第三方用户唯一凭证密钥
    public  static  String appsecret = Parameter.appsecret;

    private static Token accessToken = null;
    public static String jsapi_ticket=null;

    public static Token getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(Token accessToken) {
        TokenThread.accessToken = accessToken;
    }

    @Override
    public void run() {
        while (true){
            try {
                accessToken = WeiXinUtil.getToken(appid , appsecret);
                //获取JSAPI_Ticket
                jsapi_ticket = WeiXinUtil.JSApiTIcket(accessToken.getAccessToken());
                if(null != accessToken){
                    String dataStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                    log.info("【"+dataStr+"】,获取access_token成功，有效时长{}秒 token:{}" ,accessToken.getExpiresIn());
                    System.out.println("【"+dataStr+"】,获取access_token成功，有效时长{}秒 token:{}" +accessToken.getExpiresIn());
                    //休眠7000秒
                    Thread.sleep((accessToken.getExpiresIn()-200)*1000);
                }else{
                    //如果access_token为null, 60秒后在获取
                    Thread.sleep(60 * 1000);
                }
            } catch (InterruptedException e) {
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e1) {
                    //log.error("{}" , e1);
                    System.out.println(e1);
                }
                //log.error("{}" , e);
                System.out.println(e);
            }
        }

    }
}
