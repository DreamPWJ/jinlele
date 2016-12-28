package com.jinlele.controller;

import com.jinlele.model.User;
import com.jinlele.service.interfaces.IUserService;
import com.jinlele.util.weixinUtils.pay.PayCommonUtil;
import com.jinlele.util.weixinUtils.service.CoreService;
import com.jinlele.util.weixinUtils.util.AdvancedUtil;
import com.jinlele.util.weixinUtils.util.Parameter;
import com.jinlele.util.weixinUtils.util.SignUtil;
import com.jinlele.util.weixinUtils.util.ValidationUtil;
import com.jinlele.util.weixinUtils.vo.WeiXinOauth2Token;
import com.jinlele.util.weixinUtils.vo.WeiXinUtil;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;


@Controller
public class WeiXinController {

    @Resource
    IUserService userService;

    String timeMillis = String.valueOf(System.currentTimeMillis() / 1000);
    String randomString = PayCommonUtil.getRandomString(32);
    public static String openIds;
    public static Integer userIds;

    /**
     * 微信验签
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/api/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        // signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        // timestamp	时间戳
        // nonce  随机数
        // echostr
        System.out.println("get请求...");
        //1.获得微信签名的加密字符串
        String signature = request.getParameter("signature");
        //2.获得时间戳信息
        String timestamp = request.getParameter("timestamp");
        //3.获得随机数
        String nonce = request.getParameter("nonce");
        //4.获得随机字符串
        String echostr = request.getParameter("echostr");

        System.out.println("获得微信签名的加密字符串: " + signature);
        System.out.println("获得时间戳信息: " + timestamp);
        System.out.println("获得随机数: " + nonce);
        System.out.println("获得随机字符串: " + echostr);

        PrintWriter out = response.getWriter();

        //验证请求确认成功，原样返回echostr参数内容，则接入成功，成为开发者成功，否则请求失败
        if (ValidationUtil.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return null;
    }


    /**
     * 微信响应用户信息服务
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public String hander(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
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
        System.out.println("Post===" + Post);

        String Msg = "";
        WXBizMsgCrypt wxcpt = null;
        try {
            wxcpt = new WXBizMsgCrypt(Parameter.token, Parameter.encodingAESKey, Parameter.corId);
            //解密消息
            Msg = wxcpt.decryptMsg(signature, timestamp, nonce, Post);
        } catch (AesException e) {
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
            encryptMsg = wxcpt.encryptMsg(respMessage, timestamp, nonce);
            System.out.println("encryptMsg:" + encryptMsg);
        } catch (AesException e) {
            e.printStackTrace();
        }
        //响应信息
        return encryptMsg;
    }


    /**
     * js端 连接jssdk 数据准备
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/jsconnect", method = RequestMethod.POST)
    public Map<String, String> connect(String url) throws Exception {
        Map<String, String> ret = new HashMap<String, String>();
        String token = WeiXinUtil.getToken(Parameter.corId, Parameter.appsecret).getAccessToken();
        String jsapi_ticket = WeiXinUtil.JSApiTIcket(token);
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String nonceStr = UUID.randomUUID().toString();
        String signature = SignUtil.getSignature(
                jsapi_ticket, nonceStr, timestamp,
                url);
        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonceStr);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appid", Parameter.corId);
        return ret;
    }


    /**
     * js端 通过网页授权获取用户信息方法
     *
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/oauthServlet")
    public String oauthServlet(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException {
        //将请求、响应的编码设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        //用户同意授权后，能获取到code
        String code = request.getParameter("code");
        System.out.println("code====" + code);
        //用户同意授权
        if (!"authdeny".equals(code)) {
            //获取网页授权access_token
            WeiXinOauth2Token weiXinOauth2Token = AdvancedUtil.getOauth2AccessToken(Parameter.corId, Parameter.appsecret, code);
            //用户标示
            String openId = weiXinOauth2Token.getOpenId();
            openIds = openId;
            System.out.println("openid===" + openId);
            //去数据库查询有无数据，没有就去保存
            User userInfo = userService.getUserInfo(openId);
            System.out.println("数据库中的数据===" + new JSONObject().fromObject(userInfo));
            if (userInfo == null) {
                //获取接口访问凭证
                String Token = WeiXinUtil.getToken(Parameter.corId, Parameter.appsecret).getAccessToken();
                //获取用户信息
                userInfo = AdvancedUtil.getUserInfo(Token, openId);
                userService.insertSelective(userInfo);
            }
            userIds =  userInfo.getId();

        } else {
            return null;
        }
        return "redirect:/mall";

    }

    /**
     * 页面授权回调页面
     *
     * @return
     */
    @RequestMapping(value = "/mall")
    public String toindex() {
        return "index";
    }

    /**
     * 调用微信支付服务器端接口
     *
     * @param sn          订单号
     * @param totalAmount 支付金额
     * @param description 产品描述
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/weixin/weixinPay/{sn}/{totalAmount}/{description}/{openId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SortedMap<String, Object> toWeiXinPay(@PathVariable String sn, @PathVariable BigDecimal totalAmount, @PathVariable String description, @PathVariable String openId, HttpServletRequest request) {
        Map<String, String> map = PayCommonUtil.weixinPrePay(sn, totalAmount, description, openId, randomString, request);
        SortedMap<String, Object> finalpackage = new TreeMap<String, Object>();//通过子类TreeMap实例化接口对象 可用于排序
        finalpackage.put("appId", PayCommonUtil.APPID);
        finalpackage.put("timeStamp", timeMillis);
        finalpackage.put("nonceStr", randomString);
        finalpackage.put("package", "prepay_id=" + map.get("prepay_id"));
        finalpackage.put("signType", "MD5");
        String sign = PayCommonUtil.createSign("UTF-8", finalpackage);
        finalpackage.put("paySign", sign);
        return finalpackage;
    }


}
