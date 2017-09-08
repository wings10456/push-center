package com.hxgy.manager;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 集群通道管理类
 * 保存节点服务器和channel的关系  serverIp <-> channel
 *
 * @author WindsYan
 * @version $Id: com.hxgy.manager, v 0.1 2017/8/25 15:57 WindsYan Exp $
 */
public class ClusterChannelManager {

    private static ConcurrentMap<String, Channel> clusterChannels = new ConcurrentHashMap<>();

    public static void addChannel(String host,Channel channel){
        if(channel.isActive()){
            clusterChannels.putIfAbsent(host,channel);
        }
    }

    public static Channel getChannel(String host) {
        return clusterChannels.get(host);
    }

    public static void removeChannel(String host) {
        clusterChannels.get(host).close();
        clusterChannels.remove(host);
    }

    public static ConcurrentMap<String, Channel> getClusterChannels() {
        return clusterChannels;
    }
}
