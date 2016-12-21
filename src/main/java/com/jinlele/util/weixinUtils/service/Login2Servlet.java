package com.jinlele.util.weixinUtils.service;


import com.jinlele.util.weixinUtils.util.ValidationUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 测试号专用  微信请求验证
 */
public class Login2Servlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
//        signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
//        timestamp	时间戳
//        nonce	随机数
//        echostr
        System.out.println("get请求...");
        //1.获得微信签名的加密字符串
        String signature = request.getParameter("signature");
        //2.获得时间戳信息
        String timestamp = request.getParameter("timestamp");
        //3.获得随机数
        String nonce = request.getParameter("nonce");
        //4.获得随机字符串
        String echostr = request.getParameter("echostr");
        PrintWriter out = response.getWriter();

        //验证请求确认成功，原样返回echostr参数内容，则接入成功，成为开发者成功，否则请求失败
        if(ValidationUtil.checkSignature(signature, timestamp, nonce)){
            out.print(echostr);
        }
        System.out.println("获得微信签名的加密字符串: "+signature );
        System.out.println("获得时间戳信息: "+timestamp );
        System.out.println("获得随机数: "+nonce );
        System.out.println("获得随机字符串: "+echostr );

    }


    /**
     * 接收微信服务器发过来的xml数据包(通过post请求发送过来的)
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        //1.获得微信签名的加密字符串
        String signature = request.getParameter("signature");
        //2.获得时间戳信息
        String timestamp = request.getParameter("timestamp");
        //3.获得随机数
        String nonce = request.getParameter("nonce");

        //获得响应输出流
        PrintWriter out = response.getWriter();

        //验证请求确认成功
        if(ValidationUtil.checkSignature(signature,timestamp,nonce)){
            String respXml = Core2Service.processRequest(request);
            out.print(respXml);
        }
        out.close();
        out=null;
    }
}
