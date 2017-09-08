/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hxgy.domain.Message;
import com.hxgy.domain.MessageInfo;
import com.hxgy.service.PushService;

/**
 * 消息推送controller
 * 
 * @author WindsYan
 * @version $Id: PushController.java, v 0.1 2017年7月31日 上午9:52:22 WindsYan Exp $
 */
@Controller
public class PushController {
	@Autowired
	private PushService pushService;
	
    @RequestMapping(method = RequestMethod.POST, value = { "/push/broadcast" })
    @ResponseBody
    public void pushBroadcast(@RequestBody Message msg) {

    	pushService.pushBroadcastInfo(msg);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = { "/push" })
    @ResponseBody
    public void push() {
		Message message = new Message();
		message.setFrom("系统消息");
		message.setTo("所有人");
		message.setPushTime(System.currentTimeMillis());
		message.setType(Message.Type.SYSTEM_MSG);
		MessageInfo msg = new MessageInfo();
		msg.setSubject("333333");
		msg.setContent("33333");
		msg.setTime(System.currentTimeMillis()+"");
		message.setMessageInfo(msg);
    	pushService.pushBroadcastInfo(message);
    }
}
