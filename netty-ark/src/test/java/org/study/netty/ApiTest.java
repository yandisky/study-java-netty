package org.study.netty;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.study.netty.domain.InfoProtocol;
import org.study.netty.domain.msgobj.Feedback;
import org.study.netty.domain.msgobj.QueryInfoReq;
import org.study.netty.util.MsgUtil;

import java.nio.charset.Charset;

public class ApiTest {
    public static void main(String[] args) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.AUTO_READ, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    socketChannel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
                    socketChannel.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
                    socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            InfoProtocol requestInfoProtocol = MsgUtil.getMsg(msg.toString());
                            Integer msgType = requestInfoProtocol.getMsgType();
                            if (2 != msgType) return;
                            String queryInfoReqStr = requestInfoProtocol.getMsgObj().toString();
                            QueryInfoReq queryInfoReq = JSON.parseObject(queryInfoReqStr, QueryInfoReq.class);
                            Integer stateType = queryInfoReq.getStateType();
                            Feedback feedback = null;
                            if (1 == stateType) {
                                feedback = new Feedback(ctx.channel().id().toString(), 1, "温度信息：" + (double) (Math.random() * 100) + "°C");
                            } else if (2 == stateType) {
                                feedback = new Feedback(ctx.channel().id().toString(), 2, "光谱数据：" + (int) (Math.random() * 100) + "-" + (int) (Math.random() * 100) + "-" + (int) (Math.random() * 100) + "-" + (int) (Math.random() * 100));
                            }
                            InfoProtocol responseInfoProtocol = new InfoProtocol();
                            responseInfoProtocol.setChannelId(ctx.channel().id().toString());
                            responseInfoProtocol.setMsgType(3);
                            responseInfoProtocol.setMsgObj(feedback);
                            ctx.writeAndFlush(JSON.toJSON(responseInfoProtocol) + "\r\n");
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7397).sync();
            channelFuture.channel().closeFuture().syncUninterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
