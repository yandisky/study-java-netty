package org.study.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 客户端接收到消息：" + msg);
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7397).sync();
            System.out.println("client start done");
            //向服务端发送信息
            String str = "《===================================================================================================================》";
            channelFuture.channel().writeAndFlush("消息1" + str + "“我的结尾是一个换行符，用于传输半包粘包处理”\r\n");
            channelFuture.channel().writeAndFlush("消息2" + str + "“我的结尾是一个换行符，用于传输半包粘包处理”\r\n");
            channelFuture.channel().writeAndFlush("消息3" + str + "“我的结尾是一个换行符，用于传输半包粘包处理”\r\n");
            channelFuture.channel().writeAndFlush("消息4" + str + "“我的结尾是一个换行符，用于传输半包粘包处理”\r\n");
            channelFuture.channel().writeAndFlush("消息5" + str + "“我的结尾是一个换行符，用于传输半包粘包处理”\r\n");

            channelFuture.channel().closeFuture().syncUninterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
