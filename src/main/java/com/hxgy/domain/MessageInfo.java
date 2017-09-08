/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.domain;

/**
 * 推送消息详细对象
 * 
 * @author WindsYan
 * @version $Id: MessageInfo.java, v 0.1 2017年7月31日 上午9:57:08 WindsYan Exp $
 */
public class MessageInfo {
	
	/** 消息主题 */
	private String subject;
	
	/** 消息内容 */
	private String content;
	
	/** 时间 */
	private String time;
	
	/**
	 * Getter method for property <tt>subject</tt>.
	 * 
	 * @return property value of subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * Setter method for property <tt>subject</tt>.
	 * 
	 * @param subject value to be assigned to property subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MessageInfo [subject=" + subject + ", content=" + content + ", time=" + time + "]";
	}
}
