package org.study.netty.domain;

public class InfoProtocol {
    private String channelId;//消息传输给某个，管道ID
    private Integer msgType;//消息类型；1、Text 2、QueryInfoReq 3、Feedback
    private Object msgObj;//消息对象

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Object getMsgObj() {
        return msgObj;
    }

    public void setMsgObj(Object msgObj) {
        this.msgObj = msgObj;
    }
}
