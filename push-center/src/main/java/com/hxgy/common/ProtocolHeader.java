/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.common;

/**
 * 协议头
 * 
 * @author WindsYan
 * @version $Id: ProtocolHeader.java, v 0.1 2017年8月14日 上午10:58:38 WindsYan Exp $
 */
public class ProtocolHeader {
	
    public static final int AUTH = 1; //认证连接
    public static final int CONNECTED = 2; //连接成功
    public static final int PUSH = 3; //推送消息
    public static final int CHAT = 4; //聊天消息
    
    public static final int PERSON_MESSAGE = 5; //个人消息
    public static final int GROUP_MESSAGE = 6; //群消息
}
