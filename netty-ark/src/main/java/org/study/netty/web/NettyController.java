package org.study.netty.web;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.study.netty.domain.Device;
import org.study.netty.domain.EasyResult;
import org.study.netty.domain.InfoProtocol;
import org.study.netty.domain.ServerInfo;
import org.study.netty.util.CacheUtil;
import org.study.netty.util.MsgUtil;

import java.util.Collection;

@Controller
public class NettyController {
    private Logger logger = LoggerFactory.getLogger(NettyController.class);

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/queryNettyServerList")
    @ResponseBody
    public Collection<ServerInfo> queryNettyServerList() {
        try {
            Collection<ServerInfo> serverInfos = CacheUtil.serverInfoMap.values();
            logger.info("查询服务端列表。{}", JSON.toJSONString(serverInfos));
            return serverInfos;
        } catch (Exception e) {
            logger.info("查询服务端列表失败。", e);
            return null;
        }
    }

    @RequestMapping("/queryDeviceList")
    @ResponseBody
    public Collection<Device> queryDeviceList() {
        try {
            Collection<Device> deviceInfos = CacheUtil.deviceGroup.values();
            logger.info("查询设备链接列表。{}", JSON.toJSONString(deviceInfos));
            return deviceInfos;
        } catch (Exception e) {
            logger.info("查询设备链接列表失败。", e);
            return null;
        }
    }

    @RequestMapping("/doSendMsg")
    @ResponseBody
    public EasyResult doSendMsg(String reqStr) {
        try {
            logger.info("指定channel发送信息：{}", reqStr);
            InfoProtocol infoProtocol = MsgUtil.getMsg(reqStr);
            String channelId = infoProtocol.getChannelId();
            Channel channel = CacheUtil.cacheClientChannel.get(channelId);
            channel.writeAndFlush(MsgUtil.buildMsg(infoProtocol));
            return EasyResult.buildSuccessResult();
        } catch (Exception e) {
            logger.error("发送失败：{}", reqStr, e);
            return EasyResult.buildErrResult(e);
        }
    }
}
