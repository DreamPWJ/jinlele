package com.jinlele.util.weixinUtils.message.event;


import com.jinlele.util.weixinUtils.message.common.BaseMessage;

/**
 * 事件基类
 */
public class BaseEvent extends BaseMessage {

    //Event	事件类型，subscribe(订阅)、unsubscribe(取消订阅)
    private  String Event;


    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }
}
