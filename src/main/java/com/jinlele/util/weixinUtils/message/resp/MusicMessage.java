package com.jinlele.util.weixinUtils.message.resp;


import com.jinlele.util.weixinUtils.message.common.BaseMessage;

/**
 *音乐消息实体类
 */
public class MusicMessage extends BaseMessage {
    //音乐对象
    private Music music;

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}
