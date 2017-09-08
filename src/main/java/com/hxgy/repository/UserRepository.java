/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.repository;

import java.util.List;

import com.hxgy.domain.ChatMessage;
import com.hxgy.domain.Message;
import com.hxgy.domain.User;

/**
 * 
 * @author WindsYan
 * @version $Id: UserRepository.java, v 0.1 2017年8月7日 下午1:50:30 WindsYan Exp $
 */
public interface UserRepository {
	
	void addUser(User user);
	
	User findUser(String userId);
	
	void updateUserList(List<String> userIds, Message message);
	
	List<User> findAllUser();

	/**
	 * 更新用户的最后断开连接时间
	 * 
	 * @param userId
	 */
	void updateLastInactiveTime(String userId);

	/**
	 * 将离线消息出入user文档
	 *
	 * @param userId
	 * @param chatMessage
	 */
    void updateUserOfflienMsg(String userId, ChatMessage chatMessage);

    void deletOfflineChatMessage(String userId);
}
