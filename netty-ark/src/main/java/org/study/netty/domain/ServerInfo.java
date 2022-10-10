package org.study.netty.domain;

import java.util.Date;

public class ServerInfo {
    private String typeInfo;//服务类型
    private String ip;//IP
    private int port;//端口
    private Date openDate;//启动时间

    public ServerInfo(String typeInfo, String ip, int port, Date openDate) {
        this.typeInfo = typeInfo;
        this.ip = ip;
        this.port = port;
        this.openDate = openDate;
    }

    public String getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(String typeInfo) {
        this.typeInfo = typeInfo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }
}
