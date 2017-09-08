package com.hxgy.controller;

import com.alibaba.fastjson.JSON;
import com.hxgy.manager.RoutingManager;
import com.hxgy.service.ClusterService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 集群控制器
 *
 * @author WindsYan
 * @version $Id: com.hxgy.controller, v 0.1 2017/8/25 15:53 WindsYan Exp $
 */
@Controller
public class ClusterController {

    @Autowired
    private ClusterService clusterService;

    @RequestMapping(method = RequestMethod.GET, value = { "/cluster" })
    @ResponseBody
    public String cluster() {
        return clusterService.getClusterStaus();
    }

    @RequestMapping(method = RequestMethod.GET, value = { "/cluster/ok" })
    @ResponseBody
    public String clusterOk() {
        Map<String,Channel> maps = clusterService.getActiveChannel();
        return JSON.toJSONString(maps);
    }

    @RequestMapping(method = RequestMethod.GET, value = { "/routing" })
    @ResponseBody
    public String routing() {
        Map<String,String> maps = clusterService.getRouting();
        return JSON.toJSONString(maps);
    }

    /**
     * 更新集群路由
     * @param routingMap
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = { "/updateRoutingCluster" })
    @ResponseBody
    public String updateRoutingCluster(@RequestBody Map<String,String> routingMap) {
        Map<String,String> localRoutingMap = RoutingManager.getRouting();
        RoutingManager.addRouting(routingMap);
        return JSON.toJSONString(localRoutingMap);
    }
}
