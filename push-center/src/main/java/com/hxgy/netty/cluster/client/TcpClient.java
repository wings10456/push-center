/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.netty.cluster.client;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.hxgy.common.Constants;
import com.hxgy.manager.ClusterChannelManager;
import com.hxgy.manager.RoutingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hxgy.HxgyLog;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 
 * @author WindsYan
 * @version $Id: TcpClient.java, v 0.1 2017年8月24日 上午10:37:37 WindsYan Exp $
 */
public class TcpClient {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    private final String host;
    private final int port;

    public TcpClient(String host, int port){
        this.host = host;
        this.port = port;
    }
//    public static void main(String[] args) {
//		TcpClient client = new TcpClient("10.88.69.120", 8099);
//		try {
//			client.start();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}


	public void start(){

        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 10, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "CLIENT_WORK_" + index.incrementAndGet());
            }
        });
        try {  
            Bootstrap clientBoostrap = new Bootstrap()
            		.group(workerGroup)
            		.channel(NioSocketChannel.class)
//            		.option(ChannelOption.SO_KEEPALIVE, true)
            		.handler(new TcpClientInitializer(this));
  
            ChannelFuture channelFuture = clientBoostrap.connect(host, port).sync();  
            
            channelFuture.addListener((ChannelFutureListener) future -> {
              if (future.isSuccess()) {
                  logger.info(HxgyLog.builder().msg("TcpClient启动成功！连接IP:{} 端口:{}").build(),host,port);
              } else {
                  logger.error(HxgyLog.builder().msg("TcpClient启动失败！连接IP:{} 端口:{}").build(),host,port);
              }
            });

            ClusterChannelManager.addChannel(host,channelFuture.channel());
            logger.info(HxgyLog.builder().msg("将集群通道存入本地缓存！host:{}").build(),host);
            //每次启动一个client更新一次集群路由（client启动之前连接上的客户端，需要同步到更新的服务器）
            //1.将当前已连接的user，发送到连接到的服务器（userId,localhost）本机的路由
            //2.连接到的服务器，将集群路由返回给当前client
            //3.更新集群路由
            //4.使用HTTP协议
            RoutingManager.updateClusterRouting(host);
//            Map<String,String> localRouting = RoutingManager.getLocalRouting();
//            channelFuture.channel().writeAndFlush(JSON.toJSONString(new Proto(Proto.Type.SEND_LOCAL_ROUTING,localRouting)));
        } catch (Exception e){
            logger.warn(HxgyLog.builder().method("启动TCP客户端").msg("TcpClient启动失败！连接服务器 {}:{} 错误信息：{}").build(),host,port,e.getMessage());
            workerGroup.shutdownGracefully();
        }
    }


    public void reConnect(){
        System.out.println("===============================>reconnect<================================");
        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 10, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "CLIENT_WORK_" + index.incrementAndGet());
            }
        });
        try {
            Bootstrap clientBoostrap = new Bootstrap()
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new TcpClientInitializer(this));

            ChannelFuture channelFuture = clientBoostrap.connect(host, port).sync();

            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    logger.info(HxgyLog.builder().msg("TcpClient重启成功！连接IP:{} 端口:{}").build(),host,port);
                } else {
                    logger.error(HxgyLog.builder().msg("TcpClient重启失败！连接IP:{} 端口:{} 准备重新连接....").build(),host,port);
                }
            });

            ClusterChannelManager.addChannel(host,channelFuture.channel());
            logger.info(HxgyLog.builder().msg("将集群通道存入本地缓存！host:{}").build(),host);
            // 更新集群路由
            RoutingManager.updateClusterRouting(host);
        } catch (Exception e){
            logger.warn(HxgyLog.builder().method("重启客户端").msg("TcpClient重启客户端失败！{}:{} 错误信息：{}").build(),host,port,e.getMessage());
            //指定秒数后，重连！
            try {
                Thread.sleep(Constants.getReConnWaitSeconds()*1000);
            } catch (InterruptedException e1) {
                logger.error(HxgyLog.builder().method("重启客户端").msg("Thread.sleep出错!").build());
            }
            reConnect();
            workerGroup.shutdownGracefully();
        }
    }
}
