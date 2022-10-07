package org.study.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.study.netty.msg.Request;
import org.study.netty.msg.Response;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        Response response = new Response();
        response.setRequestId(request.getRequestId());
        response.setParam(request.getResult() + " server response");
        Thread.sleep(1001);//test server response timeout
        ctx.writeAndFlush(response);
        ReferenceCountUtil.release(request);
    }
}
