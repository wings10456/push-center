/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.domain;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 通信协议
 * 
 * @author WindsYan
 * @version $Id: Proto.java, v 0.1 2017年7月28日 上午11:30:27 WindsYan Exp $
 */
public class Proto {
	
	private final static ObjectMapper mapper = new ObjectMapper();

	private int type;
	private Object data;
	
	public Proto(){
		
	}

	/**
	 * @param type
	 * @param data
	 */
	public Proto(int type, Object data) {
		this.type = type;
		this.data = data;
	}

	public static Proto fromJson(String json) {
		return JSON.parseObject(json,Proto.class);
	}

	public String toJson() {
	    return JSON.toJSONString(this);
	}

	/** 创建心跳消息PING */
	public static Proto creatHeartBeatPing(){
		Proto proto = new Proto();
		proto.setType(Type.HEART_BEAT_PING);
		proto.setData("PING");
		return proto;
	}
	/** 创建心跳消息PONG */
	public static Proto creatHeartBeatPong(){
		Proto proto = new Proto();
		proto.setType(Type.HEART_BEAT_PONG);
		proto.setData("PONG");
		return proto;
	}

	/** 创建路由消息 */
	public static Proto creatRoutingMsg(String userId,String serverId){
		Proto proto = new Proto();
		proto.setType(Type.ROUTING);
		RoutingMessage routingMessage = new RoutingMessage(userId,serverId);
		proto.setData(routingMessage);
		return proto;
	}

	/** 创建：删除路由消息 */
	public static Proto creatRoutingRemoveMsg(String userId,String serverId){
		Proto proto = new Proto();
		proto.setType(Type.ROUTING_DELETE);
		RoutingMessage routingMessage = new RoutingMessage(userId,serverId);
		proto.setData(routingMessage);
		return proto;
	}

	/** 创建聊天消息 */
	public static Proto creatChatMessage(ChatMessage chatMessage){
		Proto proto = new Proto();
		proto.setType(Type.CHAT);
		proto.setData(chatMessage);
		return proto;
	}


	public static class Type {
	    public static final int CONNECT = 1; //认证连接
	    public static final int CONNECTED = 2; //连接成功
	    public static final int PUSH = 3; //推送消息
	    public static final int CHAT = 4; //聊天消息
		public static final int HEART_BEAT_PING = 5; //心跳消息ping
		public static final int HEART_BEAT_PONG =6; //心跳消息pong
		public static final int ROUTING = 7; //路由消息
		public static final int SEND_LOCAL_ROUTING = 8; //将本地的路由发送到服务器
		public static final int SEND_CLUSTER_ROUTING = 9; //回送集群路由
		public static final int ROUTING_DELETE = 10; // 删除路由消息
	}
	/**
	 * Getter method for property <tt>type</tt>.
	 * 
	 * @return property value of type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Setter method for property <tt>type</tt>.
	 * 
	 * @param type value to be assigned to property type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Getter method for property <tt>data</tt>.
	 * 
	 * @return property value of data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Setter method for property <tt>data</tt>.
	 * 
	 * @param data value to be assigned to property data
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
