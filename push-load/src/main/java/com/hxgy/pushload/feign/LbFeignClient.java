/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.pushload.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hxgy.pushload.feign.fallback.LbFeignClientFallback;

/**
 * Feign客户端
 * 
 * @author WindsYan
 * @version $Id: LbFeignClient.java, v 0.1 2017年8月4日 上午11:11:55 WindsYan Exp $
 */
@FeignClient(name = "HXGY-PUSH-CENTER",fallback = LbFeignClientFallback.class)
public interface LbFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/serverIp")
    public String getServerIp();
}
