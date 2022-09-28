package org.study.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.study.netty.server.MyChannelInitializer;
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
            //向服务端发送信息
            String str = "《===================================================================================================================》";
            channelFuture.channel().writeAndFlush(MsgUtil.buildMsg(channelFuture.channel().id().toString(), "消息1" + str));
            channelFuture.channel().writeAndFlush(MsgUtil.buildMsg(channelFuture.channel().id().toString(), "消息2" + str));
            channelFuture.channel().writeAndFlush(MsgUtil.buildMsg(channelFuture.channel().id().toString(), "消息3" + str));
            channelFuture.channel().writeAndFlush(MsgUtil.buildMsg(channelFuture.channel().id().toString(), "消息4" + str));
            channelFuture.channel().writeAndFlush(MsgUtil.buildMsg(channelFuture.channel().id().toString(), "消息5" + str));

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
