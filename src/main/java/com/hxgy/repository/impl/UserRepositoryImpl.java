/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.repository.impl;

import java.util.List;

import com.hxgy.domain.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.hxgy.domain.Message;
import com.hxgy.domain.User;
import com.hxgy.repository.UserRepository;

/**
 * 
 * @author WindsYan
 * @version $Id: UserRepositoryImpl.java, v 0.1 2017年8月7日 下午2:13:17 WindsYan Exp $
 */
@Repository
public class UserRepositoryImpl implements UserRepository{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	/** 
	 * @see com.hxgy.repository.UserRepository#addUser(com.hxgy.domain.User)
	 */
	@Override
	public void addUser(User user) {
		mongoTemplate.insert(user);
	}

	/** 
	 * @see com.hxgy.repository.UserRepository#findUser(java.lang.String)
	 */
	@Override
	public User findUser(String userId) {
		return mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), User.class);
	}

	/** 
	 * @see com.hxgy.repository.UserRepository#UpdateUserList(java.util.List)
	 */
	@Override
	public void updateUserList(List<String> userIds,Message message) {
		Query query=new Query(Criteria.where("userId").in(userIds));
        Update update= new Update().addToSet("messageList", message);
        //更新查询返回结果集的所有
        mongoTemplate.updateMulti(query,update,User.class);
	}

	/** 
	 * @see com.hxgy.repository.UserRepository#findAllUser()
	 */
	@Override
	public List<User> findAllUser() {
		return mongoTemplate.findAll(User.class);
	}

	/** 
	 * @see com.hxgy.repository.UserRepository#updateLastInactiveTime(java.lang.String)
	 */
	@Override
	public void updateLastInactiveTime(String userId) {
		Query query = new Query(Criteria.where("userId").is(userId));
		Update update = new Update().set("lastInactiveTime", System.currentTimeMillis());
		mongoTemplate.updateFirst(query, update, User.class);
	}

	@Override
	public void updateUserOfflienMsg(String userId, ChatMessage chatMessage) {
		Query query = new Query(Criteria.where("userId").is(userId));
		Update update = new Update().push("chatMessages",chatMessage);
		mongoTemplate.updateFirst(query,update,User.class);
	}

	@Override
	public void deletOfflineChatMessage(String userId) {
		Query query = new Query(Criteria.where("userId").is(userId));
		Update update = new Update().unset("chatMessages");
		mongoTemplate.updateFirst(query,update,User.class);
	}

}
