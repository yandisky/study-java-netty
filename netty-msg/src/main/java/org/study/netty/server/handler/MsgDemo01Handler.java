package org.study.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.study.netty.domain.MsgDemo01;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MsgDemo01Handler extends SimpleChannelInboundHandler<MsgDemo01> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgDemo01 msgDemo01) throws Exception {
        System.out.println("msg demo 01 handler");
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收消息的处理器：" + this.getClass().getName());
        System.out.println("channelId：" + msgDemo01.getChannelId());
        System.out.println("消息内容：" + msgDemo01.getDemo());
    }
}
