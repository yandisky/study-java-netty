package org.study.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.study.netty.codec.ObjDecoder;
import org.study.netty.codec.ObjEncoder;
import org.study.netty.server.handler.MsgDemo01Handler;
import org.study.netty.server.handler.MsgDemo02Handler;
import org.study.netty.server.handler.MsgDemo03Handler;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new ObjDecoder());
        socketChannel.pipeline().addLast(new ObjEncoder());
        socketChannel.pipeline().addLast(new MsgDemo01Handler());
        socketChannel.pipeline().addLast(new MsgDemo02Handler());
        socketChannel.pipeline().addLast(new MsgDemo03Handler());
    }
}
