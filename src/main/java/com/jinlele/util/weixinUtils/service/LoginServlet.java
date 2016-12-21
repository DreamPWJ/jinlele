package com.jinlele.util.weixinUtils.service;


import com.jinlele.util.weixinUtils.util.Parameter;
import com.jinlele.util.weixinUtils.util.ValidationUtil;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * 加密微信请求验证
 */
public class LoginServlet extends HttpServlet {



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

        System.out.println("获得微信签名的加密字符串: "+signature );
        System.out.println("获得时间戳信息: "+timestamp );
        System.out.println("获得随机数: "+nonce );
        System.out.println("获得随机字符串: "+echostr );

        PrintWriter out = response.getWriter();

        //验证请求确认成功，原样返回echostr参数内容，则接入成功，成为开发者成功，否则请求失败
           if(ValidationUtil.checkSignature(signature, timestamp, nonce)){
               out.print(echostr);
           }
        out.close();
        out = null;
    }


    /**
     * 接收微信服务器发过来的xml数据包(通过post请求发送过来的)
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //将请求、响应的编码设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        //1.获得微信签名的加密字符串
        String signature = request.getParameter("msg_signature");
        //2.获得时间戳信息
        String timestamp = request.getParameter("timestamp");
        //3.获得随机数
        String nonce = request.getParameter("nonce");
        String encrypt_type = request.getParameter("encrypt_type");


        //从请求中读取整个post数据
        InputStream inputStream = request.getInputStream();

        //commons.io.jar方法   将流转成字符串
        String Post = IOUtils.toString(inputStream, "UTF-8");
        //Post打印结果
        System.out.println("Post==="+Post);

        String Msg = "";
        WXBizMsgCrypt wxcpt = null;
        try{
            wxcpt = new WXBizMsgCrypt(Parameter.token , Parameter.encodingAESKey , Parameter.corId);
            //解密消息
            Msg = wxcpt.decryptMsg(signature, timestamp, nonce, Post);
        }catch (AesException e) {
            e.printStackTrace();
        }
        //Msg打印结果
        System.out.println("Msg打印结果: " + Msg);

        //调用核心业务类接收信息、处理消息
        String respMessage = CoreService.processRequest(Msg);
        System.out.println("respMessage打印结果: " + respMessage);
        String encryptMsg = "";
        try {
            //加密回复信息
            encryptMsg = wxcpt.encryptMsg(respMessage , timestamp , nonce);
            System.out.println("encryptMsg:" + encryptMsg);
        } catch (AesException e) {
            e.printStackTrace();
        }
        //响应信息
        PrintWriter out = response.getWriter();
        out.print(encryptMsg);
        out.close();
    }



}
