package org.study.netty;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.study.netty.domain.ServerInfo;
import org.study.netty.server.socket.NettyServer;
import org.study.netty.server.websocket.WsNettyServer;
import org.study.netty.util.CacheUtil;
import org.study.netty.util.NetUtil;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(Application.class);

    @Value("${netty.socket.port}")
    private int nettyServerPort;
    @Value("${netty.websocket.port}")
    private int nettyWsServerPort;
    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //start socket service
        logger.info("socket service start port:{}", nettyServerPort);
        NettyServer nettyServer = new NettyServer(new InetSocketAddress(nettyServerPort));
        Future<Channel> future = executorService.submit(nettyServer);
        Channel channel = future.get();
        if (null == channel) {
            throw new RuntimeException("socket service start error channel is null");
        }
        while (!channel.isActive()) {
            logger.info("socket service starting...");
            Thread.sleep(500);
        }
        logger.info("socket service start success:{}", channel.localAddress());
        CacheUtil.serverInfoMap.put(nettyServerPort, new ServerInfo("NettySocket", NetUtil.getHost(), nettyServerPort, new Date()));
        //start websocket service
        logger.info("websocket service start port:{}", nettyWsServerPort);
        WsNettyServer wsNettyServer = new WsNettyServer(new InetSocketAddress(nettyWsServerPort));
        Future<Channel> wsFuture = executorService.submit(wsNettyServer);
        Channel wsChannel = wsFuture.get();
        if (null == wsChannel) {
            throw new RuntimeException("websocket service start error channel is null");
        }
        while (!wsChannel.isActive()) {
            logger.info("websocket service starting...");
            Thread.sleep(500);
        }
        logger.info("websocket service start success:{}", wsChannel.localAddress());
        CacheUtil.serverInfoMap.put(nettyWsServerPort, new ServerInfo("NettyWsSocket", NetUtil.getHost(), nettyWsServerPort, new Date()));
    }
}
