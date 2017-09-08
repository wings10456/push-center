/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxgy.common.ProtocolHeader;
import com.hxgy.domain.Message;
import com.hxgy.manager.ChannelManager;

/**
 * 推送消息服务层
 * 
 * @author WindsYan
 * @version $Id: PushService.java, v 0.1 2017年7月31日 上午9:59:13 WindsYan Exp $
 */
@Service
public class PushService {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 广播普通消息到所有在线用户
	 * 
	 * @param msg
	 * @return
	 */
	public boolean pushBroadcastInfo(Message msg){
		ChannelManager.broadCastMessage(msg,ProtocolHeader.PUSH);
		return true;
	}
}
