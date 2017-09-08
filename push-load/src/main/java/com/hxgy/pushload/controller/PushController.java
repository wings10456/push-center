/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.pushload.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hxgy.pushload.entity.Message;
import com.hxgy.pushload.service.PushService;

/**
 * 消息推送控制层
 * 
 * @author WindsYan
 * @version $Id: PushController.java, v 0.1 2017年8月4日 下午4:28:43 WindsYan Exp $
 */
@Controller
public class PushController {
	
	@Autowired
	private PushService pushService;

	/**
	 * 将要发送的广播消息加入消息队列
	 * 
	 * @param message
	 * @return
	 */
    @RequestMapping(method = RequestMethod.POST, value = { "/push/broadcast" })
    @ResponseBody
    public boolean pushBroadcastMsg(@RequestBody Message message) {
    	return pushService.pushBroadcastMsg(message);
    }
}
