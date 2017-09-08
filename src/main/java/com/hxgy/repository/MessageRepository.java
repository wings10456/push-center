/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.repository;

import java.util.List;

import com.hxgy.domain.Message;

/**
 * 推送消息持久层
 * 
 * @author WindsYan
 * @version $Id: MessageRepository.java, v 0.1 2017年8月8日 下午3:18:56 WindsYan Exp $
 */
public interface MessageRepository {

	/**
	 * 获取用户从上次离线开始，到目前时间结束期间错过的推送消息
	 * 
	 * @param userId
	 * @return
	 */
	List<Message> findUnpushMessages(String userId);
}
