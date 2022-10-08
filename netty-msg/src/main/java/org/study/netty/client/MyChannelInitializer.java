package org.study.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.study.netty.codec.ObjDecoder;
import org.study.netty.codec.ObjEncoder;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new ObjDecoder());
        socketChannel.pipeline().addLast(new ObjEncoder());
        socketChannel.pipeline().addLast(new MyClientHandler());
    }
}
