package com.jinlele.util.weixinUtils.pay;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.*;

/**
 * Created by Administrator on 2016/12/25 0025.
 * 微信JS SDK支付公共类
 */
public class PayCommonUtil {
    public static String APPID = "wx7a6a63e9ee94e24d"; //公共号APPID
    public static String MCH_ID = "1422893502";//公共号商户ID
    public static String API_KEY = "abcdefghijklmnopqrstuvwxyztuokem";  //微信商户微信参数配置（与微信公共号的不一样）

    /**
     * 微信支付预处理
     *
     * @param sn
     * @param totalAmount
     * @param description
     * @param openid
     * @param randomString
     * @param request
     * @return
     */
    public static Map<String, String> weixinPrePay(String sn, BigDecimal totalAmount, String description, String openid, String randomString, String orderType, HttpServletRequest request) {
        System.out.println("totalAmount=="+totalAmount);
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", PayCommonUtil.APPID);
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);// 商户号
        parameterMap.put("nonce_str", randomString);// 随机字符串
        parameterMap.put("body", description);// 商品描述
        parameterMap.put("out_trade_no", sn);// 商户订单号
        parameterMap.put("fee_type", "CNY");//人民币
        parameterMap.put("attach", orderType);//商家数据包 现在暂时有 订单类型
        BigDecimal total = totalAmount.multiply(new BigDecimal(100));//交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数
        java.text.DecimalFormat df = new java.text.DecimalFormat("0");
        parameterMap.put("total_fee", df.format(total));// 支付金额

        parameterMap.put("spbill_create_ip", request.getRemoteAddr());// 终端IP
        parameterMap.put("notify_url", "http://www.6weiyi.com/jinlele/weixin/paymentNotice");// 接收微信支付异步通知回调地址
        parameterMap.put("trade_type", "JSAPI");// 交易类型 JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
        //trade_type为JSAPI是 openid为必填项
        parameterMap.put("openid", openid);// 用户标识

        String sign = PayCommonUtil.createSign("UTF-8", parameterMap);

        parameterMap.put("sign", sign);// 签名
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);
        // System.out.println("requestXML===============" + requestXML);
        String result = PayCommonUtil.httpsRequest(
                "https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",
                requestXML);
        // System.out.println("result===============" + result);
        Map<String, String> map = null;
        try {
            map = PayCommonUtil.doXMLParse(result);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 企业付款业务是基于微信支付商户平台的资金管理能力，为了协助商户方便地实现企业向个人付款，针对部分有开发能力的商户，提供通过API完成企业付款的功能。
     * 比如目前的保险行业向客户退保、给付、理赔
     *
     * @param sn
     * @param totalAmount
     * @param description
     * @param openid
     * @param randomString
     * @param request
     * @return
     */
    public static Map<String, String> weixinEnterprisePayment(String sn, BigDecimal totalAmount, String description, String openid, String randomString, HttpServletRequest request) {
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("mch_appid", PayCommonUtil.APPID);
        parameterMap.put("mchid", PayCommonUtil.MCH_ID);// 商户号
        parameterMap.put("nonce_str", randomString);// 随机字符串
        parameterMap.put("partner_trade_no", sn);// 商户订单号
        parameterMap.put("openid", openid);// 用户标识
        parameterMap.put("check_name", "NO_CHECK");// NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账） OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
        BigDecimal total = totalAmount.multiply(new BigDecimal(100));//交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数
        java.text.DecimalFormat df = new java.text.DecimalFormat("0");
        parameterMap.put("amount", df.format(total));// 金额
        parameterMap.put("spbill_create_ip", request.getRemoteAddr());// 终端IP
        parameterMap.put("desc", description);//企业付款操作说明信息。必填。
        String sign = PayCommonUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);// 签名
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);
        System.out.println("requestXML===============" + requestXML);
        String result = null;
        try {
            result = PayCommonUtil.httpsRequestCertificaten(
                    "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers", "POST",
                    requestXML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result===============" + result);
        Map<String, String> map = null;
        try {
            map = PayCommonUtil.doXMLParse(result);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }



    /**
     * 微信退款接口
     * @param sn 订单号
     * @param refundAmout 退款金额
     * @return
     */
    public static Map<String, String> refund(String sn, BigDecimal totalAmount,BigDecimal refundAmout) {
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", PayCommonUtil.APPID);
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);// 商户号
        parameterMap.put("nonce_str", getRandomString(32));// 随机字符串
        parameterMap.put("out_trade_no", sn);// 商户订单号
        parameterMap.put("out_refund_no", sn);// 商户订单号
        BigDecimal total = totalAmount.multiply(new BigDecimal(100));//交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数
        BigDecimal refund = refundAmout.multiply(new BigDecimal(100));//交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数
        java.text.DecimalFormat df = new java.text.DecimalFormat("0");
        parameterMap.put("total_fee", df.format(total));// 订单金额
        parameterMap.put("refund_fee", df.format(refund));// 退款金额
        parameterMap.put("op_user_id", PayCommonUtil.MCH_ID);//操作员
        String sign = PayCommonUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);// 签名
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);
        System.out.println("requestXML===============" + requestXML);
        String result = null;
        try {
            result = PayCommonUtil.httpsRequestCertificaten(
                    "https://api.mch.weixin.qq.com/secapi/pay/refund", "POST",
                    requestXML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result===============" + result);
        Map<String, String> map = null;
        try {
            map = PayCommonUtil.doXMLParse(result);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }



    /**
     * 微信退款查询接口
     * @param sn 订单号
     * @return
     */
    public static Map<String, String> refundquery(String sn) {
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", PayCommonUtil.APPID);
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);// 商户号
        parameterMap.put("nonce_str", getRandomString(32));// 随机字符串
        parameterMap.put("out_trade_no", sn);// 商户订单号
        parameterMap.put("refund_id", "");// 商户订单号
        String sign = PayCommonUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);// 签名
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);
        System.out.println("requestXML===============" + requestXML);
        String result = null;
        try {
            result = PayCommonUtil.httpsRequestCertificaten(
                    "https://api.mch.weixin.qq.com/pay/refundquery", "POST",
                    requestXML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result===============" + result);
        Map<String, String> map = null;
        try {
            map = PayCommonUtil.doXMLParse(result);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 随机字符串生成
     */

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 请求xml组装
     *
     * @param parameters
     * @return
     */
    public static String getRequestXml(SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(key) || "body".equalsIgnoreCase(key) || "sign".equalsIgnoreCase(key)) {
                sb.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
            } else {
                sb.append("<" + key + ">" + value + "</" + key + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }


    /**
     * 生成签名
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public static String createSign(String characterEncoding, SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);
        //  System.out.println("createSign===============" + sb.toString());
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * 验证回调签名
     *
     * @param packageParams
     * @param charset
     * @return
     */
    public static boolean isTenpaySign(Map<String, String> packageParams, String charset) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);

        //算出摘要
        String resultSign = "";
        String tobesign = sb.toString();
        resultSign = MD5Util.MD5Encode(tobesign, charset).toUpperCase();
        String tenpaySign = ((String) packageParams.get("sign")).toUpperCase();
        return tenpaySign.equals(resultSign);
    }


    /**
     * 请求微信方法
     *
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}" + ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}" + e);
        }
        return null;
    }


    /**
     * 请求微信方法 需要证书
     *
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     * @throws Exception
     */
    public static String httpsRequestCertificaten(String requestUrl, String requestMethod, String outputStr) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        StringBuilder res = new StringBuilder("");
         FileInputStream instream = new FileInputStream(new File("C:\\work\\apache-tomcat-8.0.21\\conf\\apiclient_cert.p12"));
        //FileInputStream instream = new FileInputStream(new File("E:\\jinlele\\apiclient_cert.p12"));
        try {
            keyStore.load(instream, PayCommonUtil.MCH_ID.toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, PayCommonUtil.MCH_ID.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {

            HttpPost httpost = new HttpPost(requestUrl);
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            StringEntity entity2 = new StringEntity(outputStr, Consts.UTF_8);
            httpost.setEntity(entity2);
            System.out.println("executing request" + httpost.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpost);

            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"utf-8"));
                    String text = "";
                    res.append(text);
                    while ((text = bufferedReader.readLine()) != null) {
                        res.append(text);
                        System.out.println(text);
                    }

                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return res.toString();

    }


    /**
     * xml解析
     *
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if (null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }

    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }
}
