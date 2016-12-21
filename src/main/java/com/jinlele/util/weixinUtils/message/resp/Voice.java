package com.jinlele.util.weixinUtils.message.resp;

/**
 * 语音的实体类
 */
public class Voice {

    //语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
    private String MediaId;


    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }


}
