/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.alibaba.fastjson.JSON;
import com.hxgy.service.UserService;
import com.hxgy.utils.BlankUtil;
import com.hxgy.utils.SpringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hxgy.HxgyLog;
import com.hxgy.domain.ChatMessage;
import com.hxgy.domain.Message;
import com.hxgy.domain.Proto;
import com.hxgy.utils.NettyUtils;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 通道管理类
 * 
 * @author WindsYan
 * @version $Id: ChannelManager.java, v 0.1 2017年7月27日 下午6:14:16 WindsYan Exp $
 */
public class ChannelManager {
	private static final Logger logger = LoggerFactory.getLogger(ChannelManager.class);

    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    private static ConcurrentMap<String, Channel> userChannels = new ConcurrentHashMap<>();
    
    private static AtomicInteger userCount = new AtomicInteger(0);
    
    private static AtomicInteger channelCount =  new AtomicInteger(0);


    /**
     * 缓存中已连接的通道数+1
     * 
     * @param channel
     */
    public static void addChannelCount(Channel channel) {
        if(channel.isActive()){
        	channelCount.incrementAndGet();
        }
    }

	/**
	 * 缓存中已连接的通道数-1
	 */
	public static void subChannelCount(){
    	channelCount.decrementAndGet();
	}

    /**
     * 从缓存中移除Channel，并且关闭Channel
     *
     * @param channel
     */
    public static String removeChannel(Channel channel) {
    	String userId = "";
        try {
            rwLock.writeLock().lock();
            channel.close();
            Set<Map.Entry<String, Channel>> entrySet = userChannels.entrySet();
			for (Map.Entry<String, Channel> me : entrySet) {
				Channel ch = me.getValue();
				if (channel == ch) {
					userId = me.getKey();
					userChannels.remove(userId);
					userCount.decrementAndGet();

					break;
				}
			}
            logger.warn(HxgyLog.builder().msg("客户端：{} 失去连接！").build(), NettyUtils.parseChannelRemoteAddr(channel));
            return userId;
        } finally {
            rwLock.writeLock().unlock();
        }

    }

    /**
     * 向所有在线的用户广播推送消息
     * 
     * @param msg
     * @param type
     */
    public static void broadCastMessage(Message msg,int type) {
        try {
            rwLock.readLock().lock();
			Set<Map.Entry<String, Channel>> entrySet = userChannels.entrySet();
			for (Map.Entry<String, Channel> me : entrySet) {
				Channel ch = me.getValue();
				ch.writeAndFlush(new TextWebSocketFrame(new Proto(type, msg).toJson()));
			}
        } finally {
            rwLock.readLock().unlock();
        }
    }
	/**
	 * 循环msg列表，发送推送消息到指定的channel
	 * 
	 * @param channel
	 * @param msgList
	 */
	public static void sendMessageList(Channel channel, int type,List<Message> msgList) {
		for (Message message : msgList) {
			channel.writeAndFlush(new TextWebSocketFrame(new Proto(type,message).toJson()));
		}
	}

    /**
     * 向指定的channel发送消息
     *
     * @param code
     * @param mess
     */
    public static void sendMessage(Channel channel, int code, Object mess) {
        channel.writeAndFlush(new TextWebSocketFrame(new Proto(code,mess).toJson()));
    }

    /**
     * 扫描并关闭失效的Channel
     */
    public static void scanNotActiveChannel() {
    	String userId;
    	Set<Map.Entry<String, Channel>> entrySet = userChannels.entrySet();
		for (Map.Entry<String, Channel> me : entrySet) {
			Channel ch = me.getValue();
			if (!ch.isActive()) {
				userId = me.getKey();
				userChannels.remove(userId);
				userCount.decrementAndGet();
				channelCount.decrementAndGet();
			}
		}
    }
    
	/**
	 * 发送聊天消息
	 * 
	 * @param chatMsg
	 */
	public static void sendChatMessage(ChatMessage chatMsg) {
		String to = chatMsg.getTo();
		if(!BlankUtil.isBlank(to)){
			//获取路由
			String serverIp = RoutingManager.getServerIp(to);
			if(!BlankUtil.isBlank(serverIp)){
				if(serverIp.equals(NettyUtils.getLocalAddress())){
					//本机路由
					sendLocalChatMessage(chatMsg);
				}else {
					//其它节点的路由
					//TODO 发送消息到对应节点，对应节点再将消息转发到对应用户，此处需要处理网络断开异常情况
					Channel ch = ClusterChannelManager.getChannel(serverIp);
					if(ch != null && ch.isActive()){
						ch.writeAndFlush(JSON.toJSONString(Proto.creatChatMessage(chatMsg)));
					}else {
						//如果channel已经失效则丢弃
						logger.warn(HxgyLog.builder().method("发送聊天消息").msg("通过ip：{} 获取channel的时候 channel为空或者channel状态为inative!").build(),serverIp);
					}
				}
			}else {
				logger.warn(HxgyLog.builder().method("发送聊天消息").msg("获取路由失败！from:{} to:{}").build(),chatMsg.getFrom(),to);
				//发送离线消息
				sendOfflineMessage(chatMsg);
			}
		}
	}

	/**
	 * 通过本地缓存，查询通道，发送消息
	 * @param chatMessage
	 */
	public static void sendLocalChatMessage(ChatMessage chatMessage){
		//从内存中获取接收者的channel
		Channel ch = userChannels.get(chatMessage.getTo());
		if(ch != null){
			//发送在线消息
			sendOnlineMessage(ch,chatMessage);
		}else {
			//发送离线消息
			sendOfflineMessage(chatMessage);
		}
	}

	/**
	 * 发送离线消息
	 * @param chatMessage
	 */
	private static void sendOfflineMessage(ChatMessage chatMessage) {
		//将消息存入mongoDB
		UserService userService = SpringHelper.getBean("userService");
		userService.setOfflineMessage(chatMessage);
	}

	/**
	 * 发送在线消息
	 * @param ch
	 * @param chatMessage
	 */
	private static void sendOnlineMessage(Channel ch, ChatMessage chatMessage) {
		String msg = JSON.toJSONString(Proto.creatChatMessage(chatMessage));
		ch.writeAndFlush(new TextWebSocketFrame(msg));
	}

	/**
	 * 获取当前已经建立连接的连接数
	 * 
	 * @return
	 */
	public static Object getChannelCount() {
		return channelCount.get();
	}

	/**
	 * 获取当前已经认证的连接数
	 * 
	 * @return
	 */
	public static Object getAuthUserCount() {
		return userCount.get();
	}

    /**
     * 获取与此服务器建立连接的userId数组
     * @return
     */
	public static List<String> getUserIds(){
	    List<String> userIds = new ArrayList<>();
        Set<Map.Entry<String, Channel>> entrySet = userChannels.entrySet();
        for (Map.Entry<String, Channel> me : entrySet) {
            String userId = me.getKey();
            userIds.add(userId);
        }
        return userIds;
    }

	/**
	 * 绑定userId与channel的关系，存本地内存
	 * 
	 * @param userId
	 * @param channel
	 */
	public static boolean bindingUserChannel(String userId, Channel channel) {
        if (!channel.isActive()) {
        	logger.error(HxgyLog.builder().msg("保存channel失败！channel 没有激活！, address: {}").build(),channel.remoteAddress());
            return false;
        }
        Channel ch = userChannels.get(userId);
        if(ch != null){
        	//用户已经认证过了，再次认证无效
        	logger.warn(HxgyLog.builder().msg("当前用户:{}已经认证过，请稍候再试！").build(),userId);
        	return false;
        }
		userChannels.putIfAbsent(userId, channel);
		userCount.incrementAndGet();
		return true;
	}
}
