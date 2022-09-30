package org.study.netty.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

public class MyChannelFutureListener implements ChannelFutureListener {
    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
            System.out.println("断线重连 listener client success");
            return;
        }
        final EventLoop eventLoop = channelFuture.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    new NettyClient().connect("127.0.0.1", 7397);
                    System.out.println("断线重连 listener client start done");
                    Thread.sleep(500);
                } catch (Exception e) {
                    System.out.println("断线重连 listener client start error");
                }
            }
        }, 1L, TimeUnit.SECONDS);
    }
}
