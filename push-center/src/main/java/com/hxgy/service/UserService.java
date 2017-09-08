/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.service;

import java.util.List;

import com.hxgy.domain.ChatMessage;
import com.hxgy.domain.Proto;

import io.netty.channel.Channel;

/**
 * 用户服务层
 * 
 * @author WindsYan
 * @version $Id: UserService1.java, v 0.1 2017年8月14日 下午2:40:25 WindsYan Exp $
 */
public interface UserService {

	/**
	 * 处理用户登录认证
	 * 1,将userId与channel的关系维护在本地内存
	 * 2,将userId与serverIp的关系维护在redis服务器
	 * 3,通知当前用户认证成功
	 * 4,如果是新用户，异步将用户基本信息存入mongodb
	 * 5,如果不是新用户，异步查询用户未读的系统推送消息，并推送给用户
	 * 
	 * @param proto
	 * @param channel
	 */
	public void handleAuth(Proto proto,Channel channel);

	/**
	 * 处理用户离线
	 * 1,移除本地userId->channel关系的数据
	 * 2,移除redis服务器上userId->serverIp关系的数据
	 * 3,更新用户最后离线时间
	 * 
	 * @param channel
	 */
	public void handleInactive(Channel channel);
	
	/**
	 * 更新用户的最后断开连接时间
	 * 
	 * @param userId
	 */
	public void updateLastInactiveTime(String userId);
	
	/**
	 * 获取当前server的所有在线用户的userId
	 * 
	 * @return
	 */
	public List<String> getOnlineUserIds();

	/**
	 * 将离线消息保存到对应用户文档
	 * @param chatMessage
	 */
    void setOfflineMessage(ChatMessage chatMessage);
}
