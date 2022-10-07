package org.study.netty;

import org.study.netty.server.ServerSocket;

public class NettyServerTest {
    public static void main(String[] args) {
        System.out.println("server start done");
        new Thread(new ServerSocket()).start();
    }
}
