package com.jinlele.util.weixinUtils.message.req;


import com.jinlele.util.weixinUtils.message.common.BaseMessage;

/**
 * 视频的消息实体类
 */
public class VideoMessage extends BaseMessage {

    private Video video;

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
