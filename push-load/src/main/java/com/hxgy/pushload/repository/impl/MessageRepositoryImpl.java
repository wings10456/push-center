/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.pushload.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.hxgy.pushload.entity.Message;
import com.hxgy.pushload.repository.MessageRepository;

/**
 * 
 * @author WindsYan
 * @version $Id: MessageRepositoryImpl.java, v 0.1 2017年8月8日 下午3:57:25 WindsYan Exp $
 */
@Repository
public class MessageRepositoryImpl implements MessageRepository{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	/** 
	 * @see com.hxgy.pushload.repository.MessageRepository#addMessage(com.hxgy.pushload.entity.Message)
	 */
	@Override
	public void addMessage(Message message) {
		mongoTemplate.insert(message);
	}

}
