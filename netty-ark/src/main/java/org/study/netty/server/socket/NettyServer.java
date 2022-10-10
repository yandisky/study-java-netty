package org.study.netty.server.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

public class NettyServer implements Callable<Channel> {
    private Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private InetSocketAddress address;

    public NettyServer(InetSocketAddress address) {
        this.address = address;
    }

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    @Override
    public Channel call() throws Exception {
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 128);
            bootstrap.childHandler(new MyChannelInitializer());
            channelFuture = bootstrap.bind(address).syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                logger.info("socket server start done");
            } else {
                logger.error("socket server start error");
            }
        }
        return channel;
    }

    public void destroy() {
        if (null == channel) return;
        channel.close();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public Channel getChannel() {
        return channel;
    }
}
