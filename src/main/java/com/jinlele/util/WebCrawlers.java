package com.jinlele.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by pwj  on 2015/11/4 0004.
 * 网络爬虫获取网络资源
 */
public class WebCrawlers {



    /**
     * 获取中塑在线网站今日油价
     */
    public static String getContractHtml(long orderNo) {
        Document doc;
        String html="";
        try {
         doc = Jsoup.connect("http://localhost:8080/backManage.war/admin/contract/showContract/"+orderNo).get();
  //          doc = Jsoup.connect("http://localhost:9000/admin/contract/showContract/"+orderNo).get();
            html = doc.html();
            return html;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return html;
    }

    public static void   maintest() {
        System.out.println("*************************************************"+testGetTodayOilPrices());

    }

    public static void main(String[] args) {
        System.out.println("*************************************************"+testGetTodayOilPrices());

    }

    /**
     * 获取中塑在线网站今日油价
     */
    public static String testGetTodayOilPrices() {
        String htmlstr = "";//要获取的html数据
        //创建HttpClientBuider对象
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //创建HttpClient对象
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        //创建POST请求对象 参数是访问服务器地址
        //HttpPost httpPost = new HttpPost("http://www.yijinhang.com/Wechats/queryGoldPrice.do");//金价
        HttpPost httpPost = new HttpPost("http://www.yijinhang.com/Wechats/diamond.jsp");//金价抓取网址
        httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");

        //执行请求 获取服务器返还的响应对象
        try {
            //执行get请求
            CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            //响应状态

            //检查响应的状态是否正常 检查状态码的值是否等于200
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code == 200) {
                //获取响应消息实体
                HttpEntity entity = httpResponse.getEntity();
                //判断响应实体是否为空
                if (entity != null) {
                    htmlstr = EntityUtils.toString(entity, "UTF-8");
                    //System.out.println("htmlstr="+htmlstr);
                }
                return htmlstr;
            }
        } catch (Exception e) {
            return "";
        } finally {
            try {
                //关闭流并释放资源
                closeableHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return htmlstr;
    }
}
