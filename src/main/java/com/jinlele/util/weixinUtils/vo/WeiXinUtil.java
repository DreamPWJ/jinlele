package com.jinlele.util.weixinUtils.vo;


import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 通用工具类
 */
public class WeiXinUtil {

    private static Logger log =  LoggerFactory.getLogger(WeiXinUtil.class);

    //凭证获取(get)
    public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    //获取JSAPI_Ticket
    public static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";



    /**
     * 发送https请求
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static JSONObject httpsRequset(String requestUrl , String requestMethod , String  outputStr){
        JSONObject jsonObject = null;
        TrustManager[] tm = {new MyX509TrustManager()};
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL" , "SunJSSE");
            sslContext.init(null , tm , new java.security.SecureRandom());

            //从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            //设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            //当outputStr不为null时向输出流写数据
            if(null != outputStr ){
                OutputStream outputStream = conn.getOutputStream();
                //注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            //从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream ,"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            StringBuffer buffer = new StringBuffer();
            while((str=bufferedReader.readLine())!=null){
                buffer.append(str);
            }

            //释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());

        } catch (ConnectException e) {
           //log.error("连接超时: {}" , e);
           System.out.println("连接超时1111111: {}" + e);
        } catch (Exception e) {
            //log.error("请求异常: {}" , e);
            System.out.println("连接超时2222222: {}" + e);
        }
        return  jsonObject;
    }


    /**
     * 获取接口访问凭证
     */
    public  static Token getToken(String appid , String appsecret){
        Token token = null;
        String requestUrl = token_url.replace("APPID" ,appid).replace("APPSECRET",appsecret);

        //发起GET请求获取凭证
         JSONObject jsonObject = httpsRequset(requestUrl , "GET" , null);
        if(null != jsonObject){
            try{
                token = new Token();
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInt("expires_in"));
            }catch (Exception e){
                token = null;
                //log.error("获取token失败 errorcode:{} errmsg:{}" ,jsonObject.getInt("errcode"));
                System.out.println("获取token失败 errorcode:{} errmsg:{}"  + jsonObject.getInt("errcode"));
            }
        }
        return  token;
    }

    /**
     * 获取JSAPI_Ticket
     * @param accessToken
     * @return
     */
    public static String JSApiTIcket(String accessToken){
        int result = 0;
        String jsApiTicket = null;
        //拼装创建菜单Url
        String url =  jsapi_ticket_url.replace("ACCESS_TOKEN", accessToken);
        //调用接口获取jsapi_ticket
        JSONObject jsonObject = httpsRequset(url, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                jsApiTicket = jsonObject.getString("ticket");
            } catch (JSONException e) {
                if (0 != jsonObject.getInt("errcode")) {
                    result = jsonObject.getInt("errcode");
                    log.error("JSAPI_Ticket获取失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
                }
            }
        }
        return jsApiTicket;
    }



    /**
     *URL编码(utf-8)
     *
     */
    public static String urlEncodeUTF8(String source){
        String result = source;
        try {
            result = URLEncoder.encode(source , "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据内容类型判断文件扩展名
     * @param   contentType  内容类型
     * @return
     */
    public static String getFileExt(String contentType){
        String fileExt = "";
        if("image/jpeg".equals(contentType))
            fileExt = ".jpg";
        else if("audio/mpeg".equals(contentType))
            fileExt = ".mp3";
        else if("audio/amr".equals(contentType))
            fileExt = ".amr";
        else if("video/mp4".equals(contentType))
            fileExt = ".mp4";
        else if("video/mpeg4".equals(contentType))
            fileExt = ".mp4";
        return  fileExt;
    }

    /**
     * MD5字符串加密
     *
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    public final static String md5(String str) throws NoSuchAlgorithmException {
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] btInput = str.getBytes();
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest md5Inst = MessageDigest.getInstance("MD5");
        // 使用指定的字节更新摘要
        md5Inst.update(btInput);
        // 获得密文
        byte[] bytes = md5Inst.digest();

        StringBuffer strResult = new StringBuffer();
        // 把密文转换成十六进制的字符串形式
        for (int i = 0; i < bytes.length; i++) {
            strResult.append(hexDigits[(bytes[i] >> 4) & 0x0f]);
            strResult.append(hexDigits[bytes[i] & 0x0f]);
        }
        return strResult.toString();
    }

    public static void main(String[] args) {
        //String url = "http://139.224.54.94/hflBlog2/oauthServlet";
        //www.6weiyi.com%2FoauthServlet
        //http%3A%2F%2Fwww.6weiyi.com%2FoauthServlet
        String url = "http://www.6weiyi.com/oauthServlet";
        String urlStr = urlEncodeUTF8(url);
        System.out.println(urlStr);
    }


}
