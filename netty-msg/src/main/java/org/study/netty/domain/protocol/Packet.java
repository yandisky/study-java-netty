package org.study.netty.domain.protocol;

public abstract class Packet {
    /**
     * 获取指令
     *
     * @return 返回指令值
     */
    public abstract Byte getCommand();
}
