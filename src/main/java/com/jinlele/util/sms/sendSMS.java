package com.jinlele.util.sms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

/**
 * 短信发送
 *
 * 平台网址: http://web.1xinxi.cn/default.aspx
 *
 */
public class sendSMS {

    private final static String name ="13620425474";
    private final static  String pwd ="D1430F683924C7D9054F44B4C319";
    private final static  String sign ="金乐乐珠宝";


    /**
     * 添加模板
     */
    public static void addTemplate() throws IOException {

        String content = "您的验证码为@，20分钟内有效，请保管好信息，以免泄露。【"+sign+"】";
        // 创建StringBuffer对象用来操作字符串
        StringBuffer sb = new StringBuffer("http://sms.1xinxi.cn/asmx/smsservice.aspx?");

        // 向StringBuffer追加用户名
        sb.append("name=" + name);

        // 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
        sb.append("&pwd="+pwd);


        // 向StringBuffer追加消息内容转URL标准码
        sb.append("&content="+URLEncoder.encode(content));


        //type为固定值pt  extno为扩展码，必须为数字 可为空
        sb.append("&type=operate_templet");
        // 创建url对象
        URL url = new URL(sb.toString());

        // 打开url连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置url请求方式 ‘get’ 或者 ‘post’
        connection.setRequestMethod("POST");

        // 发送
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        // 返回发送结果
        String inputline = in.readLine();

        // 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
        System.out.println(inputline);

    }


    /**
     * 查看模板审核
     * tid 看上个方法的返回值
     *
     */
    public static void checkTemplate() throws IOException {

        String content = "您的验证码为@，20分钟内有效，请保管好信息，以免泄露。【"+sign+"】";
        // 创建StringBuffer对象用来操作字符串
        StringBuffer sb = new StringBuffer("http://pushTempletUrl?");

        // 向StringBuffer追加用户名
        sb.append("name=" + name);

        // 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
        sb.append("&pwd="+pwd);
         //模板编号
        sb.append("&tid="+5410);
        //只有1和2 两种情况   1审核通过  2驳回
        sb.append("&state="+5410);
        //消息：驳回的原因； 有可能为空
        sb.append("&msg="+5410);
        // 向StringBuffer追加消息内容转URL标准码
        sb.append("&content="+URLEncoder.encode(content));


        //type为固定值pt  extno为扩展码，必须为数字 可为空
        sb.append("&type=operate_templet");
        // 创建url对象
        URL url = new URL(sb.toString());

        // 打开url连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置url请求方式 ‘get’ 或者 ‘post’
        connection.setRequestMethod("POST");

        // 发送
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        // 返回发送结果
        String inputline = in.readLine();

        // 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
        System.out.println(inputline);

    }



    //短信发送 四位生成随机数规则
    public static String  randomCode(){
        String code = "";
        int number = 0;
        for (int i=0;i<4;i++){
            number = new Random().nextInt(10);
            code = code + number;
        }
        return  code;
    }


    //发送短信
    public static String  sendSimple(String randomCode,String phone) throws IOException {

        // 创建StringBuffer对象用来操作字符串
        StringBuffer sb = new StringBuffer("http://sms.1xinxi.cn/asmx/smsservice.aspx?");

        // 向StringBuffer追加用户名
        sb.append("name=" + name);

        // 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
        sb.append("&pwd="+pwd);

        // 向StringBuffer追加手机号码
        sb.append("&mobile=" + phone);

        //向StringBuffer追加消息内容转URL标准码
        String content =  "您的验证码为"+randomCode+"，20分钟内有效，请保管好信息，以免泄露。";
        sb.append("&content="+URLEncoder.encode(content));

        //追加发送时间，可为空，为空为及时发送
        sb.append("&stime=");

        //加签名
        sb.append("&sign="+URLEncoder.encode(sign));

        //type为固定值pt  extno为扩展码，必须为数字 可为空
        sb.append("&type=pt&extno=");
        // 创建url对象
        URL url = new URL(sb.toString());

        // 打开url连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置url请求方式 ‘get’ 或者 ‘post’
        connection.setRequestMethod("POST");

        // 发送
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        // 返回发送结果
        String inputline = in.readLine();
       String statusCode = inputline.substring(0,1);
       if("0".equals(statusCode)){
           return randomCode;
       }else {
           return "发送失败";
       }
    }




    public static void main(String[] args) throws IOException {
        //测试添加模板
        // addTemplate();
        //测试发送短信
        sendSimple(randomCode(),"15166195973");
       // String s = "0,2017011210432609743481292,0,1,0,提交成功";
        //System.out.println(s.substring(0,1));
    }


}
