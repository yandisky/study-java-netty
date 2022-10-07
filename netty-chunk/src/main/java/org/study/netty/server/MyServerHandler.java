package org.study.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端链接断开" + ctx.channel().localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收到消息：" + msg);
        String str = "服务端收到：" + " " + new Date() + " " + msg + "\r\n";
        ctx.writeAndFlush(str);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel socketChannel = (SocketChannel) ctx.channel();
        System.out.println("链接IP:" + socketChannel.localAddress().getHostString());
        System.out.println("链接Port:" + socketChannel.localAddress().getPort());
        String str = "服务端链接建立成功" + " " + new Date() + " " + socketChannel.localAddress().getHostString() + "\r\n";
        ctx.writeAndFlush(str);
    }
}
