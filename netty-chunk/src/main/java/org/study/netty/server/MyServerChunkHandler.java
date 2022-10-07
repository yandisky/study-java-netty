package org.study.netty.server;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.stream.ChunkedStream;

public class MyServerChunkHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            super.write(ctx, msg, promise);
            return;
        }
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = getData(byteBuf);
        ByteInputStream byteInputStream = new ByteInputStream();
        byteInputStream.setBuf(bytes);
        //消息分块：按10个字节进行分块
        ChunkedStream chunkedStream = new ChunkedStream(byteInputStream, 2);
        //管道消息传输
        ChannelProgressivePromise channelProgressivePromise = ctx.channel().newProgressivePromise();
        channelProgressivePromise.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture channelProgressiveFuture, long l, long l1) throws Exception {
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {
                if (channelProgressiveFuture.isSuccess()) {
                    System.out.println("消息发送成功");
                } else {
                    System.out.println("消息发生失败");
                    promise.setFailure(channelProgressiveFuture.cause());
                }
            }
        });
        ctx.write(chunkedStream, channelProgressivePromise);
    }

    private byte[] getData(ByteBuf byteBuf) {
        if (byteBuf.hasArray()) {
            return byteBuf.array().clone();
        }
        byte[] bytes = new byte[byteBuf.readableBytes() - 1];
        byteBuf.readBytes(bytes);
        return bytes;
    }
}
