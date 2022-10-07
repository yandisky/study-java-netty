package org.study.netty;

import io.netty.channel.ChannelFuture;
import org.study.netty.client.NettyClient;
import org.study.netty.domain.FileTransferProtocol;
import org.study.netty.util.MsgUtil;

import java.io.File;

public class NettyClientTest {
    public static void main(String[] args) {
        ChannelFuture channelFuture = new NettyClient().connect("127.0.0.1", 7397);
        //模拟心跳服务与断线重连
        /*File file = new File("E:\\image\\zu.png");
        FileTransferProtocol fileTransferProtocol = MsgUtil.buildRequestTransferProtocol(file.getAbsolutePath(), file.getName(), file.length());
        channelFuture.channel().writeAndFlush(fileTransferProtocol);*/
    }
}
