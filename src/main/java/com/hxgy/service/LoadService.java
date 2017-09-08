/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.service;

/**
 * 使用feign的ribbon负载均衡，获取IP地址
 * 
 * @author WindsYan
 * @version $Id: LoadService.java, v 0.1 2017年8月4日 上午10:12:00 WindsYan Exp $
 */
public interface LoadService {
	
	/**
	 * 获取本机IP地址
	 * 
	 * @return
	 */
	public String getServerIp();
}
