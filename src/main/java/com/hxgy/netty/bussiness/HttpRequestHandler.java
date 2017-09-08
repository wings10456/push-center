/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.netty.bussiness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hxgy.HxgyLog;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 处理Http请求的handler
 * 
 * @author WindsYan
 * @version $Id: HttpRequestHandler.java, v 0.1 2017年7月25日 下午3:11:04 WindsYan Exp $
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    private final String wsUri;
    HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }
	/** 
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (wsUri.equalsIgnoreCase(request.getUri())) {
			//如果请求是一次升级了的 WebSocket 请求，则递增引用计数器（retain）并且将它传递给在 ChannelPipeline 中的下个 ChannelInboundHandler
            ctx.fireChannelRead(request.retain());
        } else {
        	logger.warn(HxgyLog.builder().msg("收到一个不是{}的请求").build(),wsUri);
        	request.release();
        }
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
    	Channel incoming = ctx.channel();
    	logger.warn(HxgyLog.builder().msg("客户端：{}异常").build(),incoming.remoteAddress());
        // 当出现异常就关闭连接
        ctx.close();
	}

}
