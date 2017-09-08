/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.pushload.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hxgy.pushload.entity.Message;
import com.hxgy.pushload.entity.MessageInfo;
import com.hxgy.pushload.service.PushService;

/**
 * 
 * @author WindsYan
 * @version $Id: PushControllerTests.java, v 0.1 2017年8月7日 上午9:40:18 WindsYan Exp $
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PushControllerTests {
	
	@Autowired
	private PushService pushService;
	
	@Test
	private void testPushController(){
    	Message message = new Message();
    	message.setFrom("系统消息");
    	message.setTo("所有人");
    	message.setPushTime(System.currentTimeMillis());
    	message.setType(Message.Type.PERSON_MSG);
    	MessageInfo msg = new MessageInfo();
    	msg.setSubject("测试主题");
    	msg.setContent("测试内容内容内容");
    	msg.setTime(System.currentTimeMillis()+"");
    	message.setMessageInfo(msg);
    	
    	pushService.pushBroadcastMsg(message);
	}
}
