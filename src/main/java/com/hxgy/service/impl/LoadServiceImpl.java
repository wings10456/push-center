/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hxgy.HxgyLog;
import com.hxgy.service.LoadService;

/**
 * 
 * @author WindsYan
 * @version $Id: LoadServiceImpl.java, v 0.1 2017年8月4日 上午10:13:07 WindsYan Exp $
 */
@Service
public class LoadServiceImpl implements LoadService{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${server.port}")
	private String port;
	
	/** 
	 * @see com.hxgy.service.LoadService#getServerIp()
	 */
	@Override
	public String getServerIp() {
		
		String address = "";
		
		try {
			address = InetAddress.getLocalHost().getHostAddress();
			address = address + ":" + port;

		} catch (UnknownHostException e) {
			logger.error(HxgyLog.builder().method("获取本机IP").msg("获取本机IP地址失败！").build(),e);
		}
		
		return address;
	}
}
