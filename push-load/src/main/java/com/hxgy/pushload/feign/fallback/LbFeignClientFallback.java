/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.pushload.feign.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hxgy.HxgyLog;
import com.hxgy.pushload.feign.LbFeignClient;

/**
 * feign失败回滚类
 * 
 * @author WindsYan
 * @version $Id: LbFeignClientFallback.java, v 0.1 2017年8月4日 上午11:12:38 WindsYan Exp $
 */
@Component
public class LbFeignClientFallback implements LbFeignClient{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 
	 * @see com.hxgy.pushload.feign.LbFeignClient#getServerIp()
	 */
	@Override
	public String getServerIp() {
		logger.error(HxgyLog.builder().method("获取服务器IPcallback()").msg("fegin失败！").build());
		return "rquest time out!";
	}

}
