/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.domain;

import java.util.List;

/**
 * 登录用户
 * 
 * @author WindsYan
 * @version $Id: User.java, v 0.1 2017年8月7日 上午11:51:45 WindsYan Exp $
 */
public class User {
	
	private String userId;
	
	private Long lastInactiveTime;

	private List<ChatMessage> chatMessages;
	
	private List<Message> messageList;


	/**
	 * Getter method for property <tt>userId</tt>.
	 * 
	 * @return property value of userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Setter method for property <tt>userId</tt>.
	 * 
	 * @param userId value to be assigned to property userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Getter method for property <tt>messageList</tt>.
	 * 
	 * @return property value of messageList
	 */
	public List<Message> getMessageList() {
		return messageList;
	}

	/**
	 * Setter method for property <tt>messageList</tt>.
	 * 
	 * @param messageList value to be assigned to property messageList
	 */
	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}
	
	
	/**
	 * Getter method for property <tt>lastInactiveTime</tt>.
	 * 
	 * @return property value of lastInactiveTime
	 */
	public Long getLastInactiveTime() {
		return lastInactiveTime;
	}

	/**
	 * Setter method for property <tt>lastInactiveTime</tt>.
	 * 
	 * @param lastInactiveTime value to be assigned to property lastInactiveTime
	 */
	public void setLastInactiveTime(Long lastInactiveTime) {
		this.lastInactiveTime = lastInactiveTime;
	}

	public List<ChatMessage> getChatMessages() {
		return chatMessages;
	}

	public void setChatMessages(List<ChatMessage> chatMessages) {
		this.chatMessages = chatMessages;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [userId=" + userId + ", messageList=" + messageList + "]";
	}
}
