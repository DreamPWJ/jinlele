package com.jinlele.util.weixinUtils.message.req;


import com.jinlele.util.weixinUtils.message.common.BaseMessage;

/**
 * 文本消息类
 */
public class TextMessage extends BaseMessage {

    //Content	文本消息内容
    private String Content;
    //MsgId	消息id，64位整型
    private long MsgId;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public long getMsgId() {
        return MsgId;
    }

    public void setMsgId(long msgId) {
        MsgId = msgId;
    }
}
