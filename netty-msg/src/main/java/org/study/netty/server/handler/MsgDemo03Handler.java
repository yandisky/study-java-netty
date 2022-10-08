package org.study.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.study.netty.domain.MsgDemo03;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MsgDemo03Handler extends SimpleChannelInboundHandler<MsgDemo03> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgDemo03 msgDemo03) throws Exception {
        System.out.println("msg demo 03 handler");
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收消息的处理器：" + this.getClass().getName());
        System.out.println("channelId：" + msgDemo03.getChannelId());
        System.out.println("消息内容：" + msgDemo03.getDemo());
    }
}
