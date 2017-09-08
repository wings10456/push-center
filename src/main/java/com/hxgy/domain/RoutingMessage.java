package com.hxgy.domain;

/**
 * 路由消息实体
 *
 * @author WindsYan
 * @version $Id: com.hxgy.domain, v 0.1 2017/8/29 14:45 WindsYan Exp $
 */
public class RoutingMessage {

    /** 用户Id */
    private String userId;

    /** 用户所在的服务器节点IP */
    private String serverIp;

    public RoutingMessage() {
    }

    public RoutingMessage(String userId, String serverIp) {
        this.userId = userId;
        this.serverIp = serverIp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    @Override
    public String toString() {
        return "RoutingMessage{" +
                "userId='" + userId + '\'' +
                ", serverIp='" + serverIp + '\'' +
                '}';
    }
}
