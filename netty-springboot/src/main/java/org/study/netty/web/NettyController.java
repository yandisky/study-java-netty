package org.study.netty.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.study.netty.server.NettyServer;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/nettyserver", method = RequestMethod.GET)
public class NettyController {
    @Resource
    private NettyServer nettyServer;

    @RequestMapping("/address")
    public String address() {
        return "server address " + nettyServer.getChannel().localAddress();
    }

    @RequestMapping("/open")
    public String open() {
        return "server open " + nettyServer.getChannel().isOpen();
    }
}
