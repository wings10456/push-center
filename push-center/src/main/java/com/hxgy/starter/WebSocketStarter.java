/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.starter;

import com.hxgy.config.Discover;
import com.hxgy.manager.ClusterChannelManager;
import com.hxgy.netty.cluster.client.TcpClient;
import com.hxgy.netty.cluster.server.TcpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hxgy.netty.bussiness.BussinessServer;
import io.netty.channel.Channel;
import java.util.List;

/**
 * 启动websocket
 * 
 * @author WindsYan
 * @version $Id: WebSocketStarter.java, v 0.1 2017年7月27日 下午4:12:46 WindsYan Exp $
 */
@Component
public class WebSocketStarter implements CommandLineRunner{
	
	@Value("${websocket.port}")
	private int port;

	@Autowired
	private Discover discover;
	
	/** 
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) throws Exception {
		
		BussinessServer pushServer = new BussinessServer(port);
		// 启动pushServer
		pushServer.init();
		pushServer.start();

		TcpServer tcpServer = new TcpServer(discover.getLocalTcpPort());
		tcpServer.init();
		tcpServer.start();

		Thread.sleep(1000);

		List<Discover.Nodes> nodes = discover.getNodes();
		for (Discover.Nodes node: nodes) {
			System.out.println(node.getHost());
			Channel channel = ClusterChannelManager.getChannel(node.getHost());
			if(channel != null && channel.isActive()){
				//这个节点已经被动建立连接了，就不再连接了,出现在断线后重启，client
				continue;
			}
			TcpClient tcpClient = new TcpClient(node.getHost(),node.getPort());
			tcpClient.start();
		}
	}

}
