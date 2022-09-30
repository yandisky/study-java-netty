package org.study.netty.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.study.netty.domain.*;
import org.study.netty.util.CacheUtil;
import org.study.netty.util.FileUtil;
import org.study.netty.util.MsgUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接IP:" + channel.localAddress().getHostString());
        System.out.println("链接Port:" + channel.localAddress().getPort());
        String str = "客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                System.out.println("断线重连 => Reader Idle");
                ctx.writeAndFlush("读取等待：客户端你在吗");
                ctx.close();
            } else if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                System.out.println("断线重连 => Write Idle");
                ctx.writeAndFlush("写入等待：客户端你在吗");
            } else if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                System.out.println("断线重连 => All Idle");
                ctx.writeAndFlush("全部时间：客户端你在吗");
            }
        }
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开链接" + ctx.channel().localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FileTransferProtocol)) return;
        FileTransferProtocol fileTransferProtocol = (FileTransferProtocol) msg;
        if (Constants.TransferType.REQUEST == fileTransferProtocol.getTransferType()) {
            //接受请求数据通过FileDescInfo进行传输、
            //1，判断FileDescInfo是否已经存在传输记录，支持断点续传
            //2，将FileDescInfo转换为FileBurstInstruct进行传输给client，作用于client回调server后对FileBurstData进行写文件操作
            FileDescInfo fileDescInfo = (FileDescInfo) fileTransferProtocol.getTransferObj();
            //断点续传信息
            FileBurstInstruct fileBurstInstructOld = CacheUtil.burstDataMap.get(fileDescInfo.getFileName());
            if (null != fileBurstInstructOld) {
                if (Constants.FileStatus.COMPLETE == fileBurstInstructOld.getStatus()) {
                    CacheUtil.burstDataMap.remove(fileDescInfo.getFileName());
                }
                //传输完成删除断点信息
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 服务端接收[断点续传]" + JSON.toJSONString(fileBurstInstructOld));
                ctx.writeAndFlush(MsgUtil.buildTransferInstruct(fileBurstInstructOld));
                return;
            }
            //发送信息
            FileTransferProtocol sendFileTransferProtocol = MsgUtil.buildTransferInstruct(Constants.FileStatus.BEGIN, fileDescInfo.getFileUrl(), 0);
            ctx.writeAndFlush(sendFileTransferProtocol);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 服务端接收" + JSON.toJSONString(fileDescInfo));
        } else if (Constants.TransferType.DATA == fileTransferProtocol.getTransferType()) {
            //接受client传输的FileBurstData，进行写入文件操作，并且判断断点续传下是否传输完成，将FileBurstInstruct传给client但是由于断点所以不会持续传输
            FileBurstData fileBurstData = (FileBurstData) fileTransferProtocol.getTransferObj();
            FileBurstInstruct fileBurstInstruct = FileUtil.writeFile("E://", fileBurstData);
            //保存断点续传信息
            CacheUtil.burstDataMap.put(fileBurstData.getFileName(), fileBurstInstruct);
            ctx.writeAndFlush(MsgUtil.buildTransferInstruct(fileBurstInstruct));
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 服务端接收" + JSON.toJSONString(fileBurstData.toString()));
            //传输完成删除断点信息
            if (fileBurstInstruct.getStatus() == Constants.FileStatus.COMPLETE) {
                CacheUtil.burstDataMap.remove(fileBurstData.getFileName());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}
