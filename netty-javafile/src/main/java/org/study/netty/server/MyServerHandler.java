package org.study.netty.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
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
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开链接" + ctx.channel().localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FileTransferProtocol)) return;
        FileTransferProtocol fileTransferProtocol = (FileTransferProtocol) msg;
        if (Constants.TransferType.REQUEST == fileTransferProtocol.getTransferType()) {
            FileDescInfo fileDescInfo = (FileDescInfo) fileTransferProtocol.getTransferObj();
            //断点续传信息
            FileBurstInstruct fileBurstInstructOld = CacheUtil.burstDataMap.get(fileDescInfo.getFileName());
            if (null != fileBurstInstructOld) {
                if (fileBurstInstructOld.getStatus() == Constants.FileStatus.COMPLETE) {
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
            FileBurstData fileBurstData = (FileBurstData) fileTransferProtocol.getTransferObj();
            FileBurstInstruct fileBurstInstruct = FileUtil.writeFile("E://", fileBurstData);
            //保存断点续传信息
            CacheUtil.burstDataMap.put(fileBurstData.getFileName(), fileBurstInstruct);
            ctx.writeAndFlush(MsgUtil.buildTransferInstruct(fileBurstInstruct));
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 服务端接收" + JSON.toJSONString(fileBurstData));
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
