package com.jinlele.util.weixinUtils.template;

import com.jinlele.model.Store;
import com.jinlele.util.weixinUtils.util.Parameter;
import com.jinlele.util.weixinUtils.vo.Token;
import com.jinlele.util.weixinUtils.vo.WeiXinUtil;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Created by panweiji on 2016/12/30 0030.
 */
public class SendOrderPaySuccessMsg {
    //日志类
    private static Logger logger = Logger.getLogger(String.valueOf(SendOrderPaySuccessMsg.class));



    /**
     * 发送模板消息
     * appId 公众账号的唯一标识
     * appSecret 公众账号的密钥
     * openId 用户标识
     */
    public void sendTemplateMessage(String appId, String appSecret, String openId) {
        Token token = WeiXinUtil.getToken(appId, appSecret);
        String access_token = token.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + access_token;
        WXTemplate temp = new WXTemplate();
        temp.setUrl("http://www.6weiyi.com");
        temp.setTouser(openId);
        temp.setTopcolor("#000000");
//      temp.setTemplate_id("ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY");
        temp.setTemplate_id("UokFDiTmQ3j1vpfUQdrcsrDxRMTHr7iSMwF0zA8Z7sE");
        Map<String, TemplateData> m = new HashMap<String, TemplateData>();
        TemplateData first = new TemplateData();
        first.setColor("#000000");
        first.setValue("金乐乐支付成功模板信息");
        m.put("first", first);
        TemplateData name = new TemplateData();
        name.setColor("#000000");
        name.setValue("99");
        m.put("orderMoneySum", name);
        TemplateData wuliu = new TemplateData();
        wuliu.setColor("#000000");
        wuliu.setValue("金乐乐商品信息测试");
        m.put("orderProductName", wuliu);
  /*      TemplateData orderNo = new TemplateData();
        orderNo.setColor("#000000");
        orderNo.setValue("**666666");
        m.put("orderNo", orderNo);
        TemplateData receiveAddr = new TemplateData();
        receiveAddr.setColor("#000000");
        receiveAddr.setValue("*测试模板");
        m.put("receiveAddr", receiveAddr);*/
        TemplateData remark = new TemplateData();
        remark.setColor("#000000");
        remark.setValue("***备注说明***");
        m.put("Remark", remark);
        temp.setData(m);
        String jsonString = JSONObject.fromObject(temp).toString();
        JSONObject jsonObject = WeiXinUtil.httpsRequset(url, "POST", jsonString);
        System.out.println(jsonObject);
        int result = 0;
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                //  logger.info("错误 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        logger.info("模板消息发送结果：" + result);
    }


    /**
     * 服务订单下单或者付款后 通知客户发货
     */
    public  static  void needSendGoodNotice(String openId, String orderno, String orderType, Store store) {
        Token token = WeiXinUtil.getToken(Parameter.corId, Parameter.appsecret);
        String access_token = token.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + access_token;
        WXTemplate temp = new WXTemplate();
        temp.setUrl("http://www.6weiyi.com/jinlele/mall#/procreceivefor"+orderno+"with"+orderType);
        temp.setTouser(openId);
        temp.setTopcolor("#000000");
        temp.setTemplate_id("ZvfP-QunBVvhirzLvfEGl5XRMTIMOl7EHaBG2NCHKKo");
        Map<String, TemplateData> m = new HashMap<String, TemplateData>();
        TemplateData first = new TemplateData();
        first.setColor("#000000");
        first.setValue("亲，请尽快寄货和完善物流");
        m.put("first", first);
        TemplateData keyword1 = new TemplateData();
        keyword1.setColor("#000000");
        keyword1.setValue(store.getName()+"\t"+store.getPhone());
        m.put("keyword1", keyword1);
        TemplateData keyword2 = new TemplateData();
        keyword2.setColor("#000000");
        keyword2.setValue(store.getAddress());
        m.put("keyword2", keyword2);
        TemplateData remark = new TemplateData();
        remark.setColor("#000000");
        remark.setValue("请核对好门店信息，如果有疑问，请联系客服。");
        m.put("remark", remark);
        temp.setData(m);
        String jsonString = JSONObject.fromObject(temp).toString();
        JSONObject jsonObject = WeiXinUtil.httpsRequset(url, "POST", jsonString);
        System.out.println(jsonObject);
        int result = 0;
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                //  logger.info("错误 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        logger.info("模板消息发送结果：" + result);
    }


    /**
     * 商城发货通知提醒
     * appId 公众账号的唯一标识
     * appSecret 公众账号的密钥
     * openId 用户标识
     */
    public  static  void sendGoodNotice(String openId ,String orderno, String orderType,String Comp,String logicno) {
        Token token = WeiXinUtil.getToken(Parameter.corId, Parameter.appsecret);
        String access_token = token.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + access_token;
        WXTemplate temp = new WXTemplate();
        temp.setUrl("http://www.6weiyi.com/jinlele/mall#/orderdetailfor"+orderno+"with"+orderType);
        //http://www.6weiyi.com/jinlele/mall#/orderdetailfor/002/20170322131500012689
        //orderdetailfor{orderNo}with{orderType}
        temp.setTouser(openId);
        temp.setTopcolor("#000000");
        temp.setTemplate_id("FnImEitwXDUMx8Chdnz2y371CRKRsYFJlpWkTmaEDAs");
        Map<String, TemplateData> m = new HashMap<String, TemplateData>();
        TemplateData first = new TemplateData();
        first.setColor("#000000");
        first.setValue("亲，宝贝已经启程了，好想快点来到你身边");
        m.put("first", first);
        TemplateData expressComp = new TemplateData();
        expressComp.setColor("#000000");
        expressComp.setValue(Comp);
        m.put("delivername", expressComp);
        TemplateData expressNo = new TemplateData();
        expressNo.setColor("#000000");
        expressNo.setValue(logicno);
        m.put("ordername", expressNo);
        TemplateData remark = new TemplateData();
        remark.setColor("#000000");
        remark.setValue("点击进入我的订单，查询物流进度，祝您生活愉快。");
        m.put("remark", remark);
        temp.setData(m);
        String jsonString = JSONObject.fromObject(temp).toString();
        JSONObject jsonObject = WeiXinUtil.httpsRequset(url, "POST", jsonString);
        System.out.println(jsonObject);
        int result = 0;
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                //  logger.info("错误 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        logger.info("模板消息发送结果：" + result);
    }



    public static void main(String[] args) {
        SendOrderPaySuccessMsg sendOrderPaySuccessMsg = new SendOrderPaySuccessMsg();
        //已经发货通知
       //sendOrderPaySuccessMsg.sendTemplateMessage(Parameter.corId, Parameter.appsecret, "okhnkvvnLaxUQxAeH6v8SUcu9jZQ");
        //sendGoodNotice("okhnkvvnLaxUQxAeH6v8SUcu9jZQ" ,"20170329100600057165", "001","圆通","000");

        //提醒客户发货通知
        Store store = new Store();
        store.setName("张三");
        store.setPhone("15166195973");
        store.setAddress("广东省广州市天河区科韵北路146号科韵路");
        sendOrderPaySuccessMsg.needSendGoodNotice("okhnkvvnLaxUQxAeH6v8SUcu9jZQ","20170329125300058890","001",store);
        //sendOrderPaySuccessMsg.sendTemplateMessage(Parameter.corId, Parameter.appsecret, "okhnkvvnLaxUQxAeH6v8SUcu9jZQ");
    }
}