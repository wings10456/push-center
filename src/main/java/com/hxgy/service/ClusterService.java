package com.hxgy.service;

import io.netty.channel.Channel;

import java.util.Map;

/**
 * 集群服务层
 *
 * @author WindsYan
 * @version $Id: com.hxgy.service, v 0.1 2017/8/25 15:54 WindsYan Exp $
 */
public interface ClusterService {

    String getClusterStaus();

    Map<String,Channel> getActiveChannel();

    Map<String,String> getRouting();
}
