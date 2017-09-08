/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.controller;

import com.hxgy.manager.ClusterChannelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hxgy.service.LoadService;

/**
 * Load控制器
 * 
 * @author WindsYan
 * @version $Id: LoadController.java, v 0.1 2017年8月4日 上午10:08:27 WindsYan Exp $
 */
@Controller
public class LoadController {
	
	@Autowired
	private LoadService loadService;
	
    @RequestMapping(method = RequestMethod.GET, value = { "/serverIp" })
    @ResponseBody
    public String getServerIp() {
    	return loadService.getServerIp();
    }

    @RequestMapping(method = RequestMethod.GET, value = { "/check" })
    @ResponseBody
    public String check(@RequestParam("host") String host) {

        ClusterChannelManager.getChannel(host).writeAndFlush("checkMsg");
        return "OK";
    }
}
