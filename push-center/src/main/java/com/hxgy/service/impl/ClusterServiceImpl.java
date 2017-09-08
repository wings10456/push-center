package com.hxgy.service.impl;

import com.alibaba.fastjson.JSON;
import com.hxgy.config.Discover;
import com.hxgy.manager.ClusterChannelManager;
import com.hxgy.manager.RoutingManager;
import com.hxgy.service.ClusterService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author WindsYan
 * @version $Id: com.hxgy.service.impl, v 0.1 2017/8/25 15:55 WindsYan Exp $
 */
@Service
public class ClusterServiceImpl implements ClusterService {

    @Autowired
    private Discover discover;

    @Override
    public String getClusterStaus() {

        List<Discover.Nodes> nodes = discover.getNodes();
        for (Discover.Nodes node : nodes) {
            if(ClusterChannelManager.getChannel(node.getHost()) != null){
                node.setStatus("正常");
            }else {
                node.setStatus("未连接");
            }
        }
        return JSON.toJSONString(nodes);
    }

    @Override
    public Map<String, Channel> getActiveChannel() {
        return ClusterChannelManager.getClusterChannels();
    }

    @Override
    public Map<String, String> getRouting() {
        return RoutingManager.getRouting();
    }
}
