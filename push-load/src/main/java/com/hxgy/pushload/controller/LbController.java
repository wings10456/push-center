/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.pushload.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hxgy.pushload.feign.LbFeignClient;

/**
 * LB控制层
 * 
 * @author WindsYan
 * @version $Id: LbController.java, v 0.1 2017年8月4日 下午1:45:42 WindsYan Exp $
 */
@Controller
public class LbController {
	
	@Autowired
	private LbFeignClient lbFeignClient;
	
	/**
	 * 获取可用的websocket服务器地址
	 * 使用feign自带的ribbon实现负载均衡
	 * 
	 * @return
	 */
    @RequestMapping(method = RequestMethod.GET, value = { "/websocket/serverIp" })
    @ResponseBody
    public String getServerIp() {
    	return lbFeignClient.getServerIp();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = { "/test" })
    @ResponseBody
    public String test() {
    	return "successssss!";
    }
}
