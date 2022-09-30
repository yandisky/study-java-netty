package org.study.netty;

import org.study.netty.server.NettyServer;

public class NettyServerTest {
    public static void main(String[] args) {
        new NettyServer().bind(7397);
    }
}
