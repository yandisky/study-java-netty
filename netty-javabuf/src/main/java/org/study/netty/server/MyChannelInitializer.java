package org.study.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.study.netty.codec.ObjDecoder;
import org.study.netty.codec.ObjEncoder;
import org.study.netty.domain.MsgInfo;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new ObjDecoder(MsgInfo.class));
        socketChannel.pipeline().addLast(new ObjEncoder(MsgInfo.class));
        socketChannel.pipeline().addLast(new MyServerHandler());
    }
}
