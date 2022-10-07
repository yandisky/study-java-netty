package org.study.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.study.netty.codec.ObjDecoder;
import org.study.netty.codec.ObjEncoder;
import org.study.netty.domain.FileTransferProtocol;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        /**
         * 心跳监测
         * 1、readerIdleTimeSeconds 读超时时间
         * 2、writerIdleTimeSeconds 写超时时间
         * 3、allIdleTimeSeconds    读写超时时间
         * 4、TimeUnit.SECONDS 秒[默认为秒，可以指定]
         */
        socketChannel.pipeline().addLast(new IdleStateHandler(2, 2, 2));
        socketChannel.pipeline().addLast(new ObjDecoder(FileTransferProtocol.class));
        socketChannel.pipeline().addLast(new ObjEncoder(FileTransferProtocol.class));
        socketChannel.pipeline().addLast(new MyServerHandler());
    }
}
