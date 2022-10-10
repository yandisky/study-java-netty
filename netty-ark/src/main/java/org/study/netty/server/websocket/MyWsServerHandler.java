package org.study.netty.server.websocket;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.netty.domain.InfoProtocol;
import org.study.netty.util.CacheUtil;
import org.study.netty.util.MsgUtil;

public class MyWsServerHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(MyWsServerHandler.class);
    private WebSocketServerHandshaker webSocketServerHandshaker;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel socketChannel = (SocketChannel) ctx.channel();
        logger.info("链接IP:{}", socketChannel.localAddress().getHostString());
        logger.info("链接Port:{}", socketChannel.localAddress().getPort());
        CacheUtil.wsChannelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("断开链接{}", ctx.channel().localAddress().toString());
        CacheUtil.wsChannelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //http
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest httpRequest = (FullHttpRequest) msg;
            if (!httpRequest.decoderResult().isSuccess()) {
                DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
                //返回应答给客户端
                if (httpResponse.status().code() != 200) {
                    ByteBuf byteBuf = Unpooled.copiedBuffer(httpResponse.status().toString(), CharsetUtil.UTF_8);
                    httpResponse.content().writeBytes(byteBuf);
                    byteBuf.release();
                }
                //如果是非keepalive，关闭连接
                ChannelFuture channelFuture = ctx.channel().writeAndFlush(httpResponse);
                if (httpResponse.status().code() != 200) {
                    channelFuture.addListener(ChannelFutureListener.CLOSE);
                }
                return;
            }
            WebSocketServerHandshakerFactory webSocketServerHandshakerFactory = new WebSocketServerHandshakerFactory("ws:/" + ctx.channel() + "/websocket", null, false);
            webSocketServerHandshaker = webSocketServerHandshakerFactory.newHandshaker(httpRequest);
            if (null == webSocketServerHandshaker) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                webSocketServerHandshaker.handshake(ctx.channel(), httpRequest);
            }
            return;
        }
        //ws
        if (msg instanceof WebSocketFrame) {
            WebSocketFrame webSocketFrame = (WebSocketFrame) msg;
            //关闭请求
            if (webSocketFrame instanceof CloseWebSocketFrame) {
                webSocketServerHandshaker.close(ctx.channel(), ((CloseWebSocketFrame) webSocketFrame).retain());
                return;
            }
            //ping请求
            if (webSocketFrame instanceof PingWebSocketFrame) {
                ctx.channel().write(new PongWebSocketFrame(webSocketFrame.content().retain()));
                return;
            }
            //只支持文本格式，不支持二进制消息
            if (!(webSocketFrame instanceof TextWebSocketFrame)) {
                throw new Exception("仅支持文本格式");
            }
            String request = ((TextWebSocketFrame) webSocketFrame).text();
            logger.info("收到：{}", request);
            InfoProtocol infoProtocol = JSON.parseObject(request, InfoProtocol.class);
            //socket转发消息
            String channelId = infoProtocol.getChannelId();
            Channel channel = CacheUtil.cacheClientChannel.get(channelId);
            if (null == channel) return;
            channel.writeAndFlush(MsgUtil.buildMsg(infoProtocol));
            //websocket消息反馈
            ctx.writeAndFlush(MsgUtil.buildWsMsgText(ctx.channel().id().toString(), "向下位机传达消息"));
            return;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        CacheUtil.wsChannelGroup.remove(ctx.channel());
        ctx.close();
        logger.info("异常信息：\r\n" + cause.getMessage());
    }
}
