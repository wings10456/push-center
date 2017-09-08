package com.hxgy.netty.protocol;

/**
 * 集群消息实体
 *
 * @author WindsYan
 * @version $Id: com.hxgy.netty.protocol, v 0.1 2017/8/25 18:09 WindsYan Exp $
 */
public class ClusterMessage {

    /** 消息类型 */
    private String type;

    public static class Type{
        public static final int HEART_BEAT = 1; //心跳
        public static final int CHAT_MSG = 2; //聊天消息
    }
}
