package org.study.netty.client;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import org.study.netty.domain.Constants;
import org.study.netty.domain.FileBurstData;
import org.study.netty.domain.FileBurstInstruct;
import org.study.netty.domain.FileTransferProtocol;
import org.study.netty.util.FileUtil;
import org.study.netty.util.MsgUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接IP:" + channel.localAddress().getHostString());
        System.out.println("链接Port:" + channel.localAddress().getPort());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开链接" + ctx.channel().localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FileTransferProtocol)) return;
        FileTransferProtocol fileTransferProtocol = (FileTransferProtocol) msg;
        if (Constants.TransferType.INSTRUCT == fileTransferProtocol.getTransferType()) {
            FileBurstInstruct fileBurstInstruct = (FileBurstInstruct) fileTransferProtocol.getTransferObj();
            if (Constants.FileStatus.COMPLETE == fileBurstInstruct.getStatus()) {
                ctx.flush();
                ctx.close();
                System.exit(-1);
                return;
            }
            FileBurstData fileBurstData = FileUtil.readFile(fileBurstInstruct.getClientFileUrl(), fileBurstInstruct.getReadPosition());
            ctx.writeAndFlush(MsgUtil.buildTransferData(fileBurstData));
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 客户端传输FILE" + fileBurstData.getFileName());
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 客户端传输SIZE(byte)" + (fileBurstData.getEndPos() - fileBurstData.getBeginPos()));
        }
        //模拟传输过程中断
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 客户端传输[主动断开链接，模拟断点续传]");
        ctx.flush();
        ctx.close();
        System.exit(-1);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}
