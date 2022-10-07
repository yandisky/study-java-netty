package org.study.netty;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;
import org.study.netty.client.ClientSocket;
import org.study.netty.future.SyncWrite;
import org.study.netty.msg.Request;
import org.study.netty.msg.Response;

public class NettyClientTest {
    private static ChannelFuture channelFuture;

    public static void main(String[] args) {
        System.out.println("client start done");
        ClientSocket clientSocket = new ClientSocket();
        new Thread(clientSocket).start();
        while (true) {
            try {
                if (null == channelFuture) {
                    channelFuture = clientSocket.getChannelFuture();
                    Thread.sleep(500);
                    continue;
                }
                Request request = new Request();
                request.setResult("client request");
                SyncWrite syncWrite = new SyncWrite();
                Response response = syncWrite.writeAndSync(channelFuture.channel(), request, 1000);
                System.out.println("查询结果：" + JSON.toJSON(response));
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
