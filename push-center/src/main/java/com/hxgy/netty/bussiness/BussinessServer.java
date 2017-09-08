/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.netty.bussiness;

import java.net.InetSocketAddress;

import com.hxgy.HxgyLog;
import com.hxgy.common.BaseServer;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty推送服务主类
 * 
 * @author WindsYan
 * @version $Id: BussinessServer.java, v 0.1 2017年7月28日 上午10:16:27 WindsYan Exp $
 */
public class BussinessServer extends BaseServer{
	
    public BussinessServer(int port) {
        this.port = port;
    }

    public void start() {    	
    	serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new BussinessServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)         
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true); 

        try {
            channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();
            
            channelFuture.addListener((ChannelFutureListener) future -> {
              if (future.isSuccess()) {
                  logger.info(HxgyLog.builder().msg("WebSocketServer启动成功！端口:{}").build(),port);
              } else {
                  logger.error(HxgyLog.builder().msg("WebSocketServer启动失败！端口:{}").build(),port);
              }
            });

        } catch (InterruptedException e) {
            logger.error(HxgyLog.builder().msg("WebSocketServer启动失败！").build(),e);
        }
    }
}
