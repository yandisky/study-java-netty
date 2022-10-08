package org.study.netty.domain;

import org.study.netty.domain.protocol.Command;
import org.study.netty.domain.protocol.Packet;

public class MsgDemo03 extends Packet {

    private String channelId;
    private String demo;

    public MsgDemo03(String channelId, String demo) {
        this.channelId = channelId;
        this.demo = demo;
    }

    @Override
    public Byte getCommand() {
        return Command.Demo03;
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
