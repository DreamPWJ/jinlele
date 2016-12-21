package com.jinlele.util.weixinUtils.service;

import com.jinlele.util.weixinUtils.message.resp.Article;
import com.jinlele.util.weixinUtils.message.resp.NewsMessage;
import com.jinlele.util.weixinUtils.message.resp.TextMessage;
import com.jinlele.util.weixinUtils.util.AdvancedUtil;
import com.jinlele.util.weixinUtils.util.MessageUtil;
import com.jinlele.util.weixinUtils.vo.TokenThread;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 测试号专用   核心服务类
 */
public class Core2Service {

        /**
         * 处理微信发来的请求
         */
        public static String processRequest(HttpServletRequest request){
            //xml格式的消息数据
            String respXml = null;

            //默认返回的文本消息内容
            String respContent = "未知的消息类型!";

            try{
                //接收并解析来自用户的xml数据包中的内容
                Map<String , String> reqMap =  MessageUtil.parseXml2(request);
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
                    respContent = "感谢您关注【hflBlog】/::D\n微信号:hufanglei_001\n"
                            + "请回复序号:\n"
                            + "1 查邮政编码  2 查询电话区号:\n"
                            + "3其他:\n  ？ 帮助菜单\n ";
                    if(Content.equals("1")){
                        respContent = "请按照格式输入地区名,例如:输入 邮编:东城";
                    }
                    if(Content.equals("2")){
                        respContent = "请按照格式输入地区名,例如:输入 区号:东城";
                    }
                    if(Content.equals("2")){
                        respContent = "请按照格式输入地区名,例如:输入 区号:东城";
                    }
                    if(Content.equals("3")){
                        respContent = "亲，生日快乐，哈哈!\n\n";
                        //respContent = "亲，想你了，哈哈!\n\n";
                        //respContent = "亲，么么哒，哈哈!\n\n";
                    }
                    if(Content.indexOf("邮编:")!=-1){
                        int pos = Content.indexOf(":");
                        String cityName = Content.substring(pos+1);
                        //respContent = dao.findByCityName(cityName);
                        System.out.println("邮编:respContent =="+respContent);
                    }
//                  textMessage.setContent("你问："+Content+",\n" +
//                            "我答:"+respContent);
                    textMessage.setContent(respContent);

//                    String token = WeiXinUtil.getToken(Parameter.corId ,Parameter.appsecret).getAccessToken();
//                    //客服发送消息
//                    String jsonMsg = AdvancedUtil.makeTextCustomMessage("o8_32sy_Exe2_Shv9XHgiIzMomoI" , "你好,客服响应到了...");
//                    AdvancedUtil.sendCustomerMessage(token , jsonMsg);

                    //将文本消息对象转换成xml
                    respXml = MessageUtil.messageToXml(textMessage);
                }
                //图片信息
                else if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)){
                    String mediaId = reqMap.get("MediaId");
                    String PicUrl = reqMap.get("PicUrl");
                    System.out.println("mediaId:"+mediaId);
                    System.out.println("picUrl:"+PicUrl);
                    AdvancedUtil adUtil = new AdvancedUtil();
                    String filePath = adUtil.getMedia(TokenThread.getAccessToken().getAccessToken(),mediaId , "c:/Download");
                    respContent = "您发送的图片已经保存到服务器";
                    textMessage.setContent(respContent);
                    respXml = MessageUtil.messageToXml(textMessage);

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
                    respContent = "您发送的是<a href=\"www.baidu.com\">连接信息</a>";
                    textMessage.setContent(respContent);
                    respXml = MessageUtil.messageToXml(textMessage);
                }
                //事件推送
                else if(MsgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)){
                    //事件类型
                    String evenType = reqMap.get("Event");
                    //关注
                    if(evenType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)){
//                        respContent = "感谢您关注【hflBlog】\n微信号:hufanglei_001\n"
//                                + "请回复序号:\n"
//                                + "1 查邮政编码  2 查询电话区号:\n"
//                                + " ？ 帮助菜单\n";
//                        textMessage.setContent(respContent);
//                        respXml = MessageUtil.messageToXml(textMessage);
                        Article item = new Article();
                        item.setTitle("胡方学院");
                        item.setDescription("胡方学院成立于2016年，是中国互联网最早的学院");
                        item.setPicUrl("http://139.224.54.94/hflBlog2/image/jin.jpg");
                        item.setUrl("http://www.juhuawang.com");
                        List<Article> articleList = new ArrayList<Article>();
                        articleList.add(item);
                        //创建图文信息
                        NewsMessage newsMessage = new NewsMessage();
                        newsMessage.setToUserName(FromUserName);
                        newsMessage.setFromUserName(ToUserName);
                        newsMessage.setCreateTime(new Date().getTime());
                        newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                        newsMessage.setArticleCount(articleList.size());
                        newsMessage.setArticles(articleList);
                        respXml = MessageUtil.messageToXml(newsMessage);
                    }
                    //取消关注
                    else if(evenType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)){
                        //TODO 取消关注后用户不会再收到公众账号发送的信息，因此不需要回复
                    }

                    //自定义菜单
                    else if(evenType.equals(MessageUtil.EVENT_TYPE_CLICK)){
                        //事件KEY值，于创建菜单时的key值对应
                        String eventKey = reqMap.get("EventKey");
                        System.out.println("eventKey==="+eventKey);

                        if(eventKey.equals("HufangCollege")){
                            Article item = new Article();
                            item.setTitle("胡方学院");
                            item.setDescription("胡方学院成立于2016年，是中国互联网最早的学院");
                            item.setPicUrl("http://139.224.54.94/hflBlog2/image/jin.jpg");
                            item.setUrl("http://www.juhuawang.com");
                            List<Article> articleList = new ArrayList<Article>();
                            articleList.add(item);
                            //创建图文信息
                            NewsMessage newsMessage = new NewsMessage();
                            newsMessage.setToUserName(FromUserName);
                            newsMessage.setFromUserName(ToUserName);
                            newsMessage.setCreateTime(new Date().getTime());
                            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                            newsMessage.setArticleCount(articleList.size());
                            newsMessage.setArticles(articleList);
                            respXml = MessageUtil.messageToXml(newsMessage);
                        }else if(eventKey.equals("HufangSoft")){
                            //创建多图文信息
                            NewsMessage newsMessage = new NewsMessage();
                            Article item1 = new Article();
                            item1.setTitle("年轻——生命给予你最美丽的礼物");
                            item1.setDescription("我也不时的在反思，为什么我的周围接触不到这个时代年轻人的声音，或是觉得特别又能打动别人的。我也时常怀疑，这样的音乐早已存在，只是我还尚未发觉。");
                            item1.setPicUrl("http://139.224.54.94/hflBlog2/image/102.jpg");
                            item1.setUrl("http://www.luoo.net/music/");

                            Article item2 = new Article();
                            item2.setTitle("是怦然心动？还是刻骨铭心？");
                            item2.setDescription("这段无疾而终的感情，让敢爱敢恨、理想主义的芒果变得淡漠了许多。她知道这样的“黄昏恋”像旧唱片，摆在角落里就好，任凭它被灰尘重重掩埋。只是偶尔拿出来怀念一番，再醉一场。如果还能碰到怦然心动的人，不至于因为莽撞而失了真爱。");
                            item2.setPicUrl("http://139.224.54.94/hflBlog2/image/33.jpg");
                            item2.setUrl("http://www.luoo.net/event/");

                            Article item3 = new Article();
                            item3.setTitle("云中行走");
                            item3.setDescription("“Follow the Tightrope Walker/He never looks to the ground”，这句包含着崇敬和自信两种情绪的句子，奠定了整张专辑的基调。");
                            item3.setPicUrl("http://139.224.54.94/hflBlog2/image/88.jpg");
                            item3.setUrl("http://www.luoo.net/essays/");

                            newsMessage.setToUserName(FromUserName);
                            newsMessage.setFromUserName(ToUserName);
                            newsMessage.setCreateTime(new Date().getTime());
                            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);

                            List<Article> articleList = new ArrayList<Article>();
                            articleList.add(item1);
                            articleList.add(item2);
                            articleList.add(item3);
                            newsMessage.setArticles(articleList);
                            newsMessage.setArticleCount(articleList.size());

                            respXml = MessageUtil.messageToXml(newsMessage);
                        }
                    }
                    //扫描带参数二维码
                    else if(evenType.equals(MessageUtil.EVENT_TYPE_SCAN)){
                        //TODO 处理扫描带参数的二维码事件
                    }
                    //上报地理位置
                    else if(evenType.equals(MessageUtil.EVENT_TYPE_LOCATION)){
                        //TODO 处理扫上报地理位置事件
                        //地理位置纬度
                        String latitude = reqMap.get("Latitude");
                        //地理位置经度
                        String Longitude = reqMap.get("Longitude");
                        //地理位置精度
                        String Precision = reqMap.get("Precision");
                        //回复文本信息


                        //回复文本信息

                        textMessage = new TextMessage();
                        textMessage.setToUserName(FromUserName);
                        textMessage.setFromUserName(ToUserName);
                        textMessage.setCreateTime(new Date().getTime());
                        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
                        respContent = "经度:" + Longitude +",纬度：" + latitude + ",精度:" + Precision;
                        System.out.println(respContent);
                        textMessage.setContent(respContent);
                        respXml = MessageUtil.messageToXml(textMessage);
                    }
                    //自定义菜单
                    else if(evenType.equals(MessageUtil.EVENT_TYPE_VIEW)){
                        //TODO 处理自定义菜单事件
                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return  respXml;
        }

    public static void main(String[] args) {
        Article article = new Article();
        article.setTitle("胡方学院");
        article.setDescription("胡方学院成立于2016年，是中国互联网最早的学院");
        article.setPicUrl("");
        article.setUrl("http://www.juhuawang.com");
        List<Article> articleList = new ArrayList<>();
        articleList.add(article);
        //创建图文信息
        NewsMessage newsMessage = new NewsMessage();
        newsMessage.setToUserName("11");
        newsMessage.setFromUserName("333");
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
        newsMessage.setArticleCount(articleList.size());
        newsMessage.setArticles(articleList);
        JSONObject s = new JSONObject().fromObject(newsMessage);
        System.out.println(s);
    }
}
