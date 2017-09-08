/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hxgy.HxgyLog;
import com.hxgy.config.RabbitConfig;
import com.hxgy.domain.Message;
import com.hxgy.service.PushService;

/**
 * rabbitMQ 消息接收者
 *
 * @author WindsYan
 * @version $Id: Receiver.java, v 0.1 2017年7月13日 下午4:12:46 WindsYan Exp $
 */
@Component
public class Receiver {
	
	private static final Logger log = LoggerFactory.getLogger(Receiver.class);
	
	@Autowired
	private PushService pushService;

	/**
	 * 接收并发送广播消息
	 */
    @RabbitListener(queues = RabbitConfig.BROADCAST_QUEUE)
    public void handleBroadcast(final Message msg) {
    	HxgyLog.Builder builder = HxgyLog.builder().caller("消息发送服务").method("接收消息");
    	log.info(builder.msg("收到一条广播消息{}").build(),msg.toString());
    	
    	// 向所有在线的用户推送广播消息
    	pushService.pushBroadcastInfo(msg);
    	log.info(HxgyLog.builder().msg("广播消息发送成功！！").build(),msg.toString());
    }
}
