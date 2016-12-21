package com.jinlele.util.weixinUtils.message.req;

/**
 * 链接实体类
 */
public class Link {
    //消息标题
    private String Title;
    //消息描述
    private String Description;
    //消息链接
    private String Url;
    //	消息id，64位整型
    private long MsgId;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public long getMsgId() {
        return MsgId;
    }

    public void setMsgId(long msgId) {
        MsgId = msgId;
    }
}
