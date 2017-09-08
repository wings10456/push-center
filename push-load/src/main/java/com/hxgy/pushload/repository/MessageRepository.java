/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.pushload.repository;

import com.hxgy.pushload.entity.Message;

/**
 * 推送消息持久层
 * 
 * @author WindsYan
 * @version $Id: MessageRepository.java, v 0.1 2017年8月8日 下午3:18:56 WindsYan Exp $
 */
public interface MessageRepository {
	
	/**
	 * 将推送消息存入数据库
	 * 
	 * @param message
	 */
	public void addMessage(Message message);
}
