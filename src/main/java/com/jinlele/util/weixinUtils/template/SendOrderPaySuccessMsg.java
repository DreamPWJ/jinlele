package com.jinlele.util.weixinUtils.template;

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
//        temp.setTemplate_id("ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY");
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

    public static void main(String[] args) {
        SendOrderPaySuccessMsg sendOrderPaySuccessMsg = new SendOrderPaySuccessMsg();
        sendOrderPaySuccessMsg.sendTemplateMessage(Parameter.corId, Parameter.appsecret, "okhnkvnWbEdBcu6Oh7j334yqyB0E");
        //   sendOrderPaySuccessMsg.sendTemplateMessage(Parameter.corId, Parameter.appsecret, "okhnkvvnLaxUQxAeH6v8SUcu9jZQ");
    }
}