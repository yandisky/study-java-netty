package org.study.netty.server.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.netty.domain.Device;
import org.study.netty.util.CacheUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(MyServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel socketChannel = (SocketChannel) ctx.channel();
        String channelId = socketChannel.id().toString();
        logger.info("链接IP:{}", socketChannel.localAddress().getHostString());
        logger.info("链接Port:{}", socketChannel.localAddress().getPort());
        //构建设备信息
        Device device = new Device();
        device.setChannelId(channelId);
        device.setNumber(UUID.randomUUID().toString());
        device.setIp(socketChannel.remoteAddress().getHostString());
        device.setPort(socketChannel.remoteAddress().getPort());
        device.setConnectTime(new Date());
        //添加设备信息
        CacheUtil.deviceGroup.put(channelId, device);
        CacheUtil.cacheClientChannel.put(channelId, socketChannel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("断开链接:{}", ctx.channel().localAddress().toString());
        String channelId = ctx.channel().id().toString();
        //移除设备信息
        CacheUtil.deviceGroup.remove(channelId);
        CacheUtil.cacheClientChannel.remove(channelId);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收到消息内容：" + msg);
        CacheUtil.wsChannelGroup.writeAndFlush(new TextWebSocketFrame(msg.toString()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String channelId = ctx.channel().id().toString();
        CacheUtil.deviceGroup.remove(channelId);
        CacheUtil.cacheClientChannel.remove(channelId);
        ctx.close();
        logger.error("异常信息：{}", cause.getMessage());
    }
}
