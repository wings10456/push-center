/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.hxgy.domain.Message;
import com.hxgy.domain.User;
import com.hxgy.repository.MessageRepository;
import com.hxgy.repository.UserRepository;

/**
 * 
 * @author WindsYan
 * @version $Id: MessageRepositoryImpl.java, v 0.1 2017年8月8日 下午4:00:51 WindsYan Exp $
 */
@Repository
public class MessageRepositoryImpl implements MessageRepository{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	/** 
	 * @see com.hxgy.repository.MessageRepository#findUnpushMessages(java.lang.String)
	 */
	@Override
	public List<Message> findUnpushMessages(String userId) {
		User user = userRepository.findUser(userId);
		Long lastInactiveTime = user.getLastInactiveTime();
		Query query= new Query(Criteria.where("pushTime").gt(lastInactiveTime));
		return mongoTemplate.find(query, Message.class);
	}

}
