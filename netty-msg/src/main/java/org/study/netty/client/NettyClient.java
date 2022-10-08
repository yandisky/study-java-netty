package org.study.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.study.netty.util.MsgUtil;

public class NettyClient {
    public static void main(String[] args) {
        new NettyClient().connect("127.0.0.1", 7397);
    }

    private void connect(String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.AUTO_READ, true);
            bootstrap.handler(new MyChannelInitializer());
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            System.out.println("client start done");
            channelFuture.channel().writeAndFlush(MsgUtil.buildMsgDemo01(channelFuture.channel().id().toString(), "消息体MsgDemo01，发送消息"));
            channelFuture.channel().writeAndFlush(MsgUtil.buildMsgDemo02(channelFuture.channel().id().toString(), "消息体MsgDemo02，发送消息"));
            channelFuture.channel().writeAndFlush(MsgUtil.buildMsgDemo03(channelFuture.channel().id().toString(), "消息体MsgDemo03，发送消息"));
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
