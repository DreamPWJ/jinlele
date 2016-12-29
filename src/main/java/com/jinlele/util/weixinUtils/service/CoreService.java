package com.jinlele.util.weixinUtils.service;
import com.jinlele.util.weixinUtils.message.resp.TextMessage;
import com.jinlele.util.weixinUtils.util.MessageUtil;
import java.util.Date;
import java.util.Map;

/**
 * 核心服务类
 */
public class CoreService {



    /**
     * 处理微信发来的请求 （加密用的）
     */
    public static String processRequest(String request){
        //xml格式的消息数据
        String respXml = null;

        //默认返回的文本消息内容
        String respContent = "未知的消息类型!";

        try{
            //接收并解析来自用户的xml数据包中的内容
            Map<String , String> reqMap =  MessageUtil.parseXml(request);
            //发送方账号
            String FromUserName = reqMap.get("FromUserName");
            //开发者微信号
            String ToUserName = reqMap.get("ToUserName");
            //消息类型
            String MsgType = reqMap.get("MsgType");
            String Content = reqMap.get("Content");

            //回复文本信息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(FromUserName);
            textMessage.setFromUserName(ToUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);

            if(MsgType.equals(MessageUtil.RESP_MESSAGE_TYPE_TEXT)){
//                nesMsg.setToUserName(FromUserName);
//                nesMsg.setFromUserName(ToUserName);
//                nesMsg.setCreateTime(new Date().getTime());
//                nesMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
//
//                nesMsg.setArticleCount(1);
//                respContent = "你好，欢迎你来到我的公众号，你们辛苦了...";
                respContent = "感谢您关注【hflBlog】\n微信号:hufanglei_001\n"
                        + "请回复序号:\n"
                        + "1 查邮政编码  2 查询电话区号:\n"
                        + " ？ 帮助菜单\n";
                if(Content.equals("1")){
                    respContent = "请按照格式输入地区名,例如:输入 邮编:东城";
                }
                if(Content.equals("2")){
                    respContent = "请按照格式输入地区名,例如:输入 区号:东城";
                }
                if(Content.equals("？")){
                    respContent = "感谢您关注【hflBlog】\n微信号:hufanglei_001\n"
                            + "请回复序号:\n"
                            + "1 查邮政编码  2 查翻译:\n"
                            + "3 查快递  ？ 帮助\n";
                }
                if(Content.startsWith("邮编:")){
                    int pos = Content.indexOf(":");
                    String cityName = Content.substring(pos+1);
                   // respContent = dao.findByCityName(cityName);
                    System.out.println("邮编:respContent =="+respContent);
                }
            }
            //图片信息
            else if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)){
                respContent = "您发送的是图片信息";
            }
            //语音信息
            else if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)){
                respContent = "您发送的是语音信息";
            }
            //视频信息REQ_MESSAGE_TYPE_SHORTVIDEO
            else if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)){
                respContent = "您发送的是视频信息";
            }
            //小视频信息
            else if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_SHORTVIDEO)){
                respContent = "您发送的是小视频信息";
            }
            //地理位置信息
            else if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)){
                respContent = "您发送的是地理位置信息";
            }
            //链接信息
            else if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)){
                respContent = "您发送的是连接信息";
            }
            //事件推送
            else if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)){
                //事件类型
                String evenType = reqMap.get("Event");
                //关注
                if(evenType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)){
                    //respContent =   "谢谢您的关注";
                    respContent = "感谢您关注【六唯壹】\n"
                            + "请回复序号:\n"
                            + "1 查邮政编码  2 查询电话区号:\n"
                            + " ？ 帮助菜单\n";
                }
                //取消关注
                else if(evenType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)){
                    //TODO 取消关注后用户不会再收到公众账号发送的信息，因此不需要回复
                }
                //扫描带参数二维码
                else if(evenType.equals(MessageUtil.EVENT_TYPE_SCAN)){
                    //TODO 处理扫描带参数的二维码事件
                }
                //上报地理位置
                else if(evenType.equals(MessageUtil.EVENT_TYPE_LOCATION)){
                    //TODO 处理扫上报地理位置事件
                }
                //自定义菜单
                else if(evenType.equals(MessageUtil.EVENT_TYPE_CLICK)){
                    //TODO 处理自定义菜单事件
                }
            }
            textMessage.setContent(respContent);
            respXml = MessageUtil.messageToXml(textMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  respXml;
    }
}
