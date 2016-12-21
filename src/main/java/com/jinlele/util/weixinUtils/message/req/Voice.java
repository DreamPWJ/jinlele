package com.jinlele.util.weixinUtils.message.req;

/**
 * 语音的实体类
 */
public class Voice {

    //语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
    private String MediaId;
    //语音格式，如amr，speex等
    private String Format;
    //Recognition	语音识别结果，UTF8编码
//    private String  Recognition;
    //消息id，64位整型
    private long  MsgID;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }

//    public String getRecognition() {
//        return Recognition;
//    }
//
//    public void setRecognition(String recognition) {
//        Recognition = recognition;
//    }

    public long getMsgID() {
        return MsgID;
    }

    public void setMsgID(long msgID) {
        MsgID = msgID;
    }
}
