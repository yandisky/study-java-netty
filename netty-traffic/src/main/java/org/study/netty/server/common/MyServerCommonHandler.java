package org.study.netty.server.common;

import io.netty.channel.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public abstract class MyServerCommonHandler extends SimpleChannelInboundHandler<String> {
    protected boolean sentFlag;
    private Runnable counterTask;
    private AtomicLong consumeMsgLength = new AtomicLong();
    private long priorProgress;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        counterTask = () -> {
            while (true) {
                try {
                    Thread.sleep(500);
                    long length = consumeMsgLength.getAndSet(0);
                    if (0 == length) continue;
                    System.out.println("数据发送速率(KB/S)：" + length);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        super.handlerAdded(ctx);
    }

    protected abstract void sendData(ChannelHandlerContext ctx);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sendData(ctx);
        new Thread(counterTask).start();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("接收到消息：" + s);
    }

    protected ChannelProgressivePromise getChannelProgressivePromise(ChannelHandlerContext ctx, Consumer<ChannelProgressiveFuture> completedAction) {
        ChannelProgressivePromise channelProgressivePromise = ctx.newProgressivePromise();
        channelProgressivePromise.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture channelProgressiveFuture, long progress, long total) throws Exception {
                //这里作用？记录成功后进度值-10为了后续显示数据发送速率
                consumeMsgLength.addAndGet(progress - priorProgress);
                priorProgress = progress;
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {
                sentFlag = false;
                if (channelProgressiveFuture.isSuccess()) {
                    priorProgress -= 10;
                    Optional.ofNullable(completedAction).ifPresent(action -> action.accept(channelProgressiveFuture));
                } else {
                    System.out.println("消息发送失败");
                    channelProgressiveFuture.cause().printStackTrace();
                }
            }
        });
        return channelProgressivePromise;
    }
}
