package org.study.netty.future;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.study.netty.msg.Request;
import org.study.netty.msg.Response;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SyncWrite {
    public Response writeAndSync(final Channel channel, final Request request, final long timeout) {
        if (channel == null || request == null) {
            throw new NullPointerException("channel or request is null");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout <= 0");
        }
        String requestId = UUID.randomUUID().toString();
        request.setRequestId(requestId);

        WriteFuture<Response> writeFuture = new SyncWriteFuture(request.getRequestId());
        SyncWriteMap.syncKey.put(request.getRequestId(), writeFuture);
        Response response;
        try {
            response = doWriteAndSync(channel, request, timeout, writeFuture);
        } catch (Exception e) {
            response = new Response();
            response.setRequestId(request.getRequestId());
            response.setParam("client exception " + e.getMessage());
        }
        SyncWriteMap.syncKey.remove(request.getRequestId());
        return response;
    }

    public Response doWriteAndSync(final Channel channel, final Request request, final long timeout, final WriteFuture<Response> writeFuture) throws Exception {
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                writeFuture.setWriteResult(channelFuture.isSuccess());
                writeFuture.setCause(channelFuture.cause());
                if (!writeFuture.isWriteSuccess()) {
                    SyncWriteMap.syncKey.remove(writeFuture.requestId());
                }
            }
        });
        Response response = writeFuture.get(timeout, TimeUnit.MILLISECONDS);
        if (response == null) {
            if (writeFuture.isTimeout()) {
                throw new TimeoutException("sync request timeout");
            } else {
                throw new Exception(writeFuture.cause());
            }
        }
        return response;
    }
}
