package org.study.netty.domain;

import org.study.netty.domain.protocol.Command;
import org.study.netty.domain.protocol.Packet;

public class MsgDemo02 extends Packet {

    private String channelId;
    private String demo;

    public MsgDemo02(String channelId, String demo) {
        this.channelId = channelId;
        this.demo = demo;
    }

    @Override
    public Byte getCommand() {
        return Command.Demo02;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }
}
