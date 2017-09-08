package com.hxgy.manager;

import com.alibaba.fastjson.JSON;
import com.hxgy.HxgyLog;
import com.hxgy.common.Constants;
import com.hxgy.domain.Proto;
import com.hxgy.utils.BlankUtil;
import com.hxgy.utils.HttpUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 路由管理器
 *
 * @author WindsYan
 * @version $Id: com.hxgy.manager, v 0.1 2017/8/29 14:32 WindsYan Exp $
 */
public class RoutingManager {

    private static final Logger logger = LoggerFactory.getLogger(RoutingManager.class);

    /** 用户ID，用户连接所在的服务器IP */
    private static ConcurrentMap<String, String> routing = new ConcurrentHashMap<>();

    /** 本地缓存添加路由 */
    public static void addRouting(String userId,String serverIp) {
        routing.putIfAbsent(userId,serverIp);
    }

    /** 本地缓存添加路由 */
    public static void addRouting(Map<String,String> routingMap) {
        if(routingMap == null || routingMap.size() == 0) return;
        Set<Map.Entry<String, String>> entrySet = routingMap.entrySet();
        for (Map.Entry<String,String> me : entrySet) {
            routing.putIfAbsent(me.getKey(),me.getValue());
        }
        routing.notify();
    }

    /** 移除路由表对应的数据 */
    public static void removeRouting(String userId){
        routing.remove(userId);
    }

    /**
     * 移除本地路由，向集群中发送移除路由的消息
     *
     * @param userId
     * @param serverIp
     */
    public static void removeRoutingFromCluster(String userId,String serverIp){
        //删除本地路由
        routing.remove(userId);
        //向所有在线的服务器集群发送移除路由的消息
        //TODO 此处需要解决一个问题，当缓存中的channel已经断网了，上线通知就不会到达
        Map<String,Channel> maps = ClusterChannelManager.getClusterChannels();
        Set<Map.Entry<String, Channel>> entrySet = maps.entrySet();
        for (Map.Entry<String, Channel> me : entrySet) {
            Channel ch = me.getValue();
            String routingRemoveMsg = JSON.toJSONString(Proto.creatRoutingRemoveMsg(userId, serverIp));
            logger.info(HxgyLog.builder().method("发送[移除路由]").msg("{}").build(),routingRemoveMsg);
            ch.writeAndFlush(routingRemoveMsg);
        }
    }

    public static ConcurrentMap<String, String> getRouting() {
        return routing;
    }

    /**
     * 通过用户ID获取服务器IP
     * @param userId
     * @return
     */
    public static String getServerIp(String userId){
        return routing.get(userId);
    }

    /**
     * 添加路由到集群,同时添加到本地
     * @param userId
     * @param serverIp
     */
    public static void addRoutingToCluster(String userId,String serverIp){
        //本地添加缓存
        addRouting(userId, serverIp);
        //向所有在线的服务器集群发送消息
        //TODO 此处需要解决一个问题，当缓存中的channel已经断网了，上线通知就不会到达，当断线的channle重连的时候会更新路由
        Map<String,Channel> maps = ClusterChannelManager.getClusterChannels();
        Set<Map.Entry<String, Channel>> entrySet = maps.entrySet();
        for (Map.Entry<String, Channel> me : entrySet) {
            Channel ch = me.getValue();
            String routingMsg = JSON.toJSONString(Proto.creatRoutingMsg(userId, serverIp));
            logger.info(HxgyLog.builder().method("发送路由消息").msg("{}").build(),routingMsg);
            ch.writeAndFlush(routingMsg);
        }
    }

    private static Map<String,String>  getLocalRouting(){
        Map<String,String> routingMap = new HashMap<>();
        String localIp = Constants.getLocalIp();
        List<String> userIds = ChannelManager.getUserIds();
        for (String userId : userIds) {
            routingMap.put(userId,localIp);
        }
        return routingMap;
    }

    /**
     * 下载更新集群路由
     * 使用http协议
     * @param host
     */
    public static void updateClusterRouting(String host) {
        Map<String,String> routingMap = getLocalRouting();
        String url = "http://"+host+":"+Constants.getServerPort()+"/updateRoutingCluster";
        String body = JSON.toJSONString(routingMap);
        String response = "";
        try {
            response = HttpUtils.doPost(url,body);
        } catch (Exception e) {
            logger.error(HxgyLog.builder().method("更新集群路由").msg("Http请求失败！").build(),e);
        }
        Map map = JSON.parseObject(response,Map.class);
        if(map !=null && map.size() != 0){
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            for (Map.Entry<String,String> me : entrySet) {
                routing.putIfAbsent(me.getKey(),me.getValue());
            }
        }
    }

    /**
     * 根据服务器IP,删除本机缓存中此IP对应的所有路由信息
     *
     * @param host
     */
    public static void removeHostRouting(String host) {
        if(BlankUtil.isBlank(host)) return;
        Set<Map.Entry<String, String>> entrySet = routing.entrySet();
        for (Map.Entry<String,String> me : entrySet) {
            if(host.equals(me.getValue())){
                routing.remove(me.getKey());
            }
        }
    }
}
