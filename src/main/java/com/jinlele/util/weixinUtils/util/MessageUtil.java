package com.jinlele.util.weixinUtils.util;


import com.jinlele.util.weixinUtils.message.req.ImageMessage;
import com.jinlele.util.weixinUtils.message.req.VideoMessage;
import com.jinlele.util.weixinUtils.message.resp.MusicMessage;
import com.jinlele.util.weixinUtils.message.resp.NewsMessage;
import com.jinlele.util.weixinUtils.message.resp.TextMessage;
import com.jinlele.util.weixinUtils.message.resp.VoiceMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息处理工具类
 */
public class MessageUtil {
    //请求消息类型: 文本
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";
    //请求消息类型: 图片
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
    //请求消息类型: 语音
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
    //请求消息类型: 视频
    public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
    //请求消息类型: 小视频
    public static final String REQ_MESSAGE_TYPE_SHORTVIDEO = "shortvideo";
    //请求消息类型: 地理位置
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";
    //请求消息类型: 链接
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    //请求消息类型: 事件推送
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    //事件类型: subscribe(订阅)
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
    //事件类型: unsubscribe(取消订阅)
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
    //事件类型: scan(用户已关注时的扫描带参数二维码)
    public static final String EVENT_TYPE_SCAN = "scan";
    //事件类型: LOCATION(上报地理位置)
    public static final String EVENT_TYPE_LOCATION = "LOCATION";
    //事件类型: CLICK(自定义菜单)
    public static final String EVENT_TYPE_CLICK = "CLICK";
    //事件类型:VIEW （菜单跳转）
    public static final String EVENT_TYPE_VIEW = "VIEW";

    //响应消息类型: 文本
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";
    //响应消息类型: 图片
    public static final String RESP_MESSAGE_TYPE_IMAGE = "image";
    //响应消息类型: 语音
    public static final String RESP_MESSAGE_TYPE_VOICE = "voice";
    //响应消息类型: 视频
    public static final String RESP_MESSAGE_TYPE_VIDEO = "video";
    //响应消息类型: 音乐
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";
    //响应消息类型: 图文
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * 从流中解析出每个节点的内容
     * <xml>
         <ToUserName><![CDATA[toUser]]></ToUserName>
         <FromUserName><![CDATA[fromUser]]></FromUserName>
         <CreateTime>1348831860</CreateTime>
         <MsgType><![CDATA[text]]></MsgType>
         <Content><![CDATA[this is a test]]></Content>
         <MsgId>1234567890123456</MsgId>
     * </xml>
     * @param request
     * @return
     * @throws IOException
     */
    public static Map<String , String> parseXml(String request) throws IOException, DocumentException {
        Map<String , String> map = new HashMap<>();
        //从输入流中获取流对象
       // InputStream in = request.getInputStream();
        //从request中取得输入流
        InputStream inputStream = new ByteArrayInputStream(request.getBytes("UTF-8"));
        //读取输入流
        SAXReader reader = new SAXReader();

        //从流中获得文档对象
        Document document = reader.read(inputStream);
        //获得根节点（xml）
        Element root = document.getRootElement();
        //获得根节点下面的所有子节点
        List<Element> children = root.elements();
        for(Element e:children){
            //遍历每一个节点，并按照节点名--节点值 放入map中
            map.put(e.getName() , e.getText());
        }
        //释放资源
        inputStream.close();
        return map;
    }

    /**
     *测试号专用  从流中解析出每个节点的内容
     * <xml>
     <ToUserName><![CDATA[toUser]]></ToUserName>
     <FromUserName><![CDATA[fromUser]]></FromUserName>
     <CreateTime>1348831860</CreateTime>
     <MsgType><![CDATA[text]]></MsgType>
     <Content><![CDATA[this is a test]]></Content>
     <MsgId>1234567890123456</MsgId>
     * </xml>
     * @param request
     * @return
     * @throws IOException
     */
    public static Map<String , String> parseXml2(HttpServletRequest request) throws IOException{
        Map<String , String> map = new HashMap<>();
        //从输入流中获取流对象
        InputStream in = request.getInputStream();
        //构建SAXReader 阅读器对象
        SAXReader reader = new SAXReader();
        try {
            //从流中获得文档对象
            Document document = reader.read(in);
            //获得根节点（xml）
            Element root = document.getRootElement();
            //获得根节点下面的所有子节点
            List<Element> children = root.elements();
            for(Element e:children){
                //遍历每一个节点，并按照节点名--节点值 放入map中
                map.put(e.getName() , e.getText());
            }
            //关闭流
            in.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }





    /**
     * 用于扩展节点数据 处理特殊字符
     */
    private static XStream xstream = new XStream(new XppDriver(){
        public HierarchicalStreamWriter createWriter(Writer out){
            return  new PrettyPrintWriter(out){
                boolean cdata = true;
                public void startNode(String name , Class classzz){
                    super.startNode(name , classzz);
                }
                protected void writeText(QuickWriter writer , String text){
                    if(cdata){
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    }else{
                        writer.write(text);
                    }
                }
            };
        }
    });

    /**
     * 将文本信息转换成XML格式
     */
    public static  String messageToXml(TextMessage textMessage){
        xstream.alias("xml" , textMessage.getClass());
        String xml = xstream.toXML(textMessage);
        System.out.println("响应所转换的xml: "+xml);
        return xml;
    }

    /**
     * 将图像信息转换成XML格式
     */
    public static  String messageToXml(ImageMessage imageMessage){
        xstream.alias("xml" , imageMessage.getClass());
        String xml = xstream.toXML(imageMessage);
        System.out.println("响应所转换的xml: "+xml);
        return xml;
    }

    /**
     * 将语音信息转换成XML格式
     */
    public static  String messageToXml(VoiceMessage voiceMessage){
        xstream.alias("xml" , voiceMessage.getClass());
        String xml = xstream.toXML(voiceMessage);
        System.out.println("响应所转换的xml: "+xml);
        return xml;
    }

    /**
     * 将视频信息转换成XML格式
     */
    public static  String messageToXml(VideoMessage videoMessage){
        xstream.alias("xml" , videoMessage.getClass());
        String xml = xstream.toXML(videoMessage);
        System.out.println("响应所转换的xml: "+xml);
        return xml;
    }

    /**
     * 将音乐信息转换成XML格式
     */
    public static  String messageToXml(MusicMessage musicMessage){
        xstream.alias("xml" , musicMessage.getClass());
        String xml = xstream.toXML(musicMessage);
        System.out.println("响应所转换的xml: "+xml);
        return xml;
    }

    /**
     * 将图文信息转换成XML格式
     */
    public static  String messageToXml(NewsMessage newsMessage){
        xstream.alias("xml" , newsMessage.getClass());
        String xml = xstream.toXML(newsMessage);
        xml= xml.replace("com.hfl.message.resp.Article","item");
        System.out.println("响应所转换的xml: "+xml);
        return xml;
    }




}
