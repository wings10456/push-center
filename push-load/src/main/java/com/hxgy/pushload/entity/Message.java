/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.pushload.entity;

import com.mongodb.util.JSON;

/**
 * 消息对象
 * 
 * @author WindsYan
 * @version $Id: Message.java, v 0.1 2017年8月4日 下午3:56:18 WindsYan Exp $
 */
public class Message {
	
	/** 消息类型：1广播消息,2单点推送 */
	private int type;
	
	/** 消息发送方  */
	private String from;

	/** 消息接收方 */
	private String to;
	
	/** 消息发送时间 */
	private Long pushTime;
	
	/** 消息内容 */
	private MessageInfo messageInfo;
	
	public static class Type {
	    public static final int SYSTEM_MSG = 1;
	    public static final int PERSON_MSG = 2;
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
	 * Getter method for property <tt>from</tt>.
	 * 
	 * @return property value of from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Setter method for property <tt>from</tt>.
	 * 
	 * @param from value to be assigned to property from
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Getter method for property <tt>to</tt>.
	 * 
	 * @return property value of to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Setter method for property <tt>to</tt>.
	 * 
	 * @param to value to be assigned to property to
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Getter method for property <tt>messageInfo</tt>.
	 * 
	 * @return property value of messageInfo
	 */
	public MessageInfo getMessageInfo() {
		return messageInfo;
	}

	/**
	 * Setter method for property <tt>messageInfo</tt>.
	 * 
	 * @param messageInfo value to be assigned to property messageInfo
	 */
	public void setMessageInfo(MessageInfo messageInfo) {
		this.messageInfo = messageInfo;
	}
	

	/**
	 * Getter method for property <tt>pushTime</tt>.
	 * 
	 * @return property value of pushTime
	 */
	public Long getPushTime() {
		return pushTime;
	}

	/**
	 * Setter method for property <tt>pushTime</tt>.
	 * 
	 * @param pushTime value to be assigned to property pushTime
	 */
	public void setPushTime(Long pushTime) {
		this.pushTime = pushTime;
	}

	/** 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Message [type=" + type + ", from=" + from + ", to=" + to + ", messageInfo=" + messageInfo + "]";
	}
	
	
}
