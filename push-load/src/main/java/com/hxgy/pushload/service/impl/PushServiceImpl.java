/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.pushload.service.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hxgy.pushload.entity.Message;
import com.hxgy.pushload.repository.MessageRepository;
import com.hxgy.pushload.service.PushService;

/**
 * 
 * @author WindsYan
 * @version $Id: PushServiceImpl.java, v 0.1 2017年8月4日 下午4:42:56 WindsYan Exp $
 */
@Service
public class PushServiceImpl implements PushService{
	
	@Autowired
    private AmqpTemplate rabbitTemplate;
	
	@Autowired
	private MessageRepository messageRepository;

    @Value("${spring.rabbitmq.broadcast.exchange_name}")
    private String exchange;


	/** 
	 * @see com.hxgy.pushload.service.PushService#pushBroadcastMsg(com.hxgy.pushload.entity.Message)
	 */
	@Override
	public boolean pushBroadcastMsg(Message message) {
		rabbitTemplate.convertAndSend(exchange, "", message);
		//发送消息后。将消息存入数据库
		messageRepository.addMessage(message);
		return true;
	}

}
