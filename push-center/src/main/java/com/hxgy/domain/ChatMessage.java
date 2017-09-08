/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.domain;

/**
 * 聊天消息实体
 * 
 * @author WindsYan
 * @version $Id: ChatMessage.java, v 0.1 2017年8月11日 下午2:16:59 WindsYan Exp $
 */
public class ChatMessage {

	/** 消息发送者 */
	private String from;
	
	/** 消息接收者 */
	private String to;
	
	/** 消息发送时间 */
	private String time;
	
	/** 消息内容 */
	private String content;
		
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
	 * Getter method for property <tt>time</tt>.
	 * 
	 * @return property value of time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * Setter method for property <tt>time</tt>.
	 * 
	 * @param time value to be assigned to property time
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * Getter method for property <tt>content</tt>.
	 * 
	 * @return property value of content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * Setter method for property <tt>content</tt>.
	 * 
	 * @param content value to be assigned to property content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
