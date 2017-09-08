/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.hxgy.common.Constants;
import com.hxgy.config.Discover;
import com.hxgy.domain.ChatMessage;
import com.hxgy.manager.RoutingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hxgy.common.ProtocolHeader;
import com.hxgy.domain.Message;
import com.hxgy.domain.Proto;
import com.hxgy.domain.User;
import com.hxgy.manager.ChannelManager;
import com.hxgy.repository.MessageRepository;
import com.hxgy.repository.UserRepository;
import com.hxgy.service.UserService;

import io.netty.channel.Channel;

/**
 * 
 * @author WindsYan
 * @version $Id: UserServiceImpl.java, v 0.1 2017年8月14日 下午2:43:10 WindsYan Exp $
 */
@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private Discover discover;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MessageRepository messageRepository;

	/**
	 * 处理用户登录认证（异步）
	 * 1,将userId与channel的关系维护在本地内存
	 * 2,将userId与serverIp的关系维护在redis服务器,或者广播到集群中其他节点
	 * 3,通知当前用户认证成功
	 * 4,如果是新用户，将用户基本信息存入mongodb
	 * 5,如果不是新用户，查询用户未读的系统推送消息，并推送给用户
	 *
	 * @see com.hxgy.service.UserService#handleAuth(com.hxgy.domain.Proto, io.netty.channel.Channel)
	 */
	@Override
	@Async("userAsyncPool")
	public void handleAuth(Proto proto, Channel channel) {
		String userId = (String)proto.getData();
		if(!StringUtils.isEmpty(userId)){
			//1
			boolean tag = ChannelManager.bindingUserChannel(userId,channel);
			if(!tag){
				ChannelManager.sendMessage(channel, ProtocolHeader.AUTH, false);
				return;
			}
			//2 将路由存本地，同时广播到集群的所有在线节点
			RoutingManager.addRoutingToCluster(userId,discover.getLocalIp());
			//3
			ChannelManager.sendMessage(channel, ProtocolHeader.AUTH, true);
			//4,5
			handleAfterAuth(userId, channel);
		}
	}
	
	/**
	 * 认证成功后的处理
	 * 通过userId取出用户还未接收的消息推送
	 * 用过userId取出用户未接收的离线聊天消息
	 * 
	 * @param channel 
	 */
	private void handleAfterAuth(String userId, Channel channel){
		User user = userRepository.findUser(userId);
		if(user != null){
			//取出用户还未接收的推送消息
			//根据用户的最后离开时间，计算出用户从离线开始到当前时间错过的推送消息
			List<Message> msgList = messageRepository.findUnpushMessages(userId);
			if(msgList != null && msgList.size() != 0){
				//发送用户还未接收的消息
				ChannelManager.sendMessageList(channel,ProtocolHeader.PUSH,msgList);
			}
			List<ChatMessage> chatMessageList = user.getChatMessages();
			if(chatMessageList !=null && chatMessageList.size() != 0){
				//若有离线消息，则发送到当前用户
				for (ChatMessage chatMsg : chatMessageList) {
					ChannelManager.sendLocalChatMessage(chatMsg);
				}
				//删除离线消息
				userRepository.deletOfflineChatMessage(user.getUserId());
			}
		}else{
			//用户不存在，创建用户
			user = new User();
			user.setUserId(userId);
			userRepository.addUser(user);
		}
	}

	/**
	 * 处理用户离线
	 * 1,移除本地userId->channel关系的数据
	 * 2,移除路由关系缓存，通知其他服务器移除缓存(移除redis服务器上userId->serverIp关系的数据)
	 * 3,更新用户最后离线时间
	 * @see com.hxgy.service.UserService#handleInactive(io.netty.channel.Channel)
	 */
	@Override
	public void handleInactive(Channel channel) {
		//1
		String userId = ChannelManager.removeChannel(channel);
		if(!StringUtils.isEmpty(userId)){
			//2
//			RoutingManager.removeRouting(userId);
			//删除集群上所有此条路由信息
			RoutingManager.removeRoutingFromCluster(userId, Constants.getLocalIp());
			//3
			this.updateLastInactiveTime(userId);
		}
	}

	/** 
	 * @see com.hxgy.service.UserService#updateLastInactiveTime(java.lang.String)
	 */
	@Override
	public void updateLastInactiveTime(String userId) {
		userRepository.updateLastInactiveTime(userId);
	}

	/** 
	 * @see com.hxgy.service.UserService#getOnlineUserIds()
	 */
	@Override
	public List<String> getOnlineUserIds() {
		List<User> users = userRepository.findAllUser();
		List<String> userIds = new ArrayList<>();
		for (User user: users) {
			userIds.add(user.getUserId());
		}
		return userIds;
	}

	@Override
	public void setOfflineMessage(ChatMessage chatMessage) {
		String userId  = chatMessage.getTo();
		userRepository.updateUserOfflienMsg(userId,chatMessage);
	}

}
