/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 推送服务器基类
 * 
 * @author WindsYan
 * @version $Id: BaseServer.java, v 0.1 2017年7月27日 下午4:16:10 WindsYan Exp $
 */
public abstract class BaseServer {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
    protected int port = 8099;

//    protected DefaultEventLoopGroup defLoopGroup;
    protected NioEventLoopGroup bossGroup;
    protected NioEventLoopGroup workGroup;
    protected ChannelFuture channelFuture;
    protected ServerBootstrap serverBootstrap;

    public void init(){

//        defLoopGroup = new DefaultEventLoopGroup(8, new ThreadFactory() {
//            private AtomicInteger index = new AtomicInteger(0);
//
//            @Override
//            public Thread newThread(Runnable r) {
//                return new Thread(r, "DEFAULTEVENTLOOPGROUP_" + index.incrementAndGet());
//            }
//        });
        bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "BOSS_" + index.incrementAndGet());
            }
        });
        workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 10, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "WORK_" + index.incrementAndGet());
            }
        });

        serverBootstrap = new ServerBootstrap();
    }

    public void shutdown() {
//        if (defLoopGroup != null) {
//            defLoopGroup.shutdownGracefully();
//        }
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
