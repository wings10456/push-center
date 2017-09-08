/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.pushload.service;

import com.hxgy.pushload.entity.Message;

/**
 * 消息推送服务层
 * 
 * @author WindsYan
 * @version $Id: PushService.java, v 0.1 2017年8月4日 下午4:30:22 WindsYan Exp $
 */
public interface PushService {

	public boolean pushBroadcastMsg(Message message);
}
