package org.study.netty.util;

import org.study.netty.domain.MsgInfo;

public class MsgUtil {
    public static MsgInfo buildMsg(String channelId, String msgContent) {
        return new MsgInfo(channelId, msgContent);
    }
}
