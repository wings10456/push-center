/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hxgy.domain.Message;
import com.hxgy.domain.MessageInfo;
import com.hxgy.domain.User;

/**
 * 
 * @author WindsYan
 * @version $Id: UserRepositoryTests.java, v 0.1 2017年8月7日 下午1:59:58 WindsYan Exp $
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void testAdd() {
		
    	Message message = new Message();
    	message.setFrom("系统消息");
    	message.setTo("所有人");
    	message.setType(Message.Type.PERSON_MSG);
    	MessageInfo msg = new MessageInfo();
    	msg.setSubject("测试主题");
    	msg.setContent("测试内容内容内容");
    	msg.setTime(System.currentTimeMillis()+"");
    	message.setMessageInfo(msg);
		
		User user = new User();
		List<Message> msgList = new ArrayList<>();
		msgList.add(message);
		user.setMessageList(msgList);
		user.setUserId("1111111111");
		
		userRepository.addUser(user);
	}
	
	@Test
	public void testFindOne(){
		String userId = "3232";
		User user = userRepository.findUser(userId);
		System.out.println(user.toString());
	}
}
