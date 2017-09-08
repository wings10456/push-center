/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.netty.bussiness;

import com.alibaba.fastjson.JSON;
import com.hxgy.domain.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hxgy.HxgyLog;
import com.hxgy.common.ProtocolHeader;
import com.hxgy.domain.Proto;
import com.hxgy.manager.ChannelManager;
import com.hxgy.service.UserService;
import com.hxgy.utils.NettyUtils;
import com.hxgy.utils.SpringHelper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 处理业务的handler
 * 继承自SimpleChannelInboundHandler,会自动释放未处理的消息，不需要手动release
 * 
 * @author WindsYan
 * @version $Id: BussinessHandler.java, v 0.1 2017年7月28日 上午10:42:12 WindsYan Exp $
 */
public class BussinessHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>  {
	
	private UserService userService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	/** 
	 * 处理接收到的文本消息
	 * 
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		String msgStr = msg.text();
		logger.info(HxgyLog.builder().msg("收到客户端发来的消息：{}").build(),msgStr);
		
		Proto proto = Proto.fromJson(msgStr);
		if(proto != null){
			switch (proto.getType()) {
			case ProtocolHeader.AUTH:
				//认证
				userService = SpringHelper.getBean("userService");
				userService.handleAuth(proto,ctx.channel());
				break;
			case ProtocolHeader.PERSON_MESSAGE:
				//推送个人消息
				break;
			case Proto.Type.CHAT:
				//聊天消息
				ChatMessage chatMsg = JSON.parseObject((String)proto.getData(),ChatMessage.class);
				if(chatMsg != null){
					ChannelManager.sendChatMessage(chatMsg);
				}
				break;
			default:
				break;
			}
		}else{
			ctx.channel().writeAndFlush(new TextWebSocketFrame("消息协议格式不正确！"));
		}
	}
	
	/** 
	 * 通道active状态时触发，将通道加入管理，并将user的addr附值为请求的IP地址+端口
	 * 只是连接成功，需要等待认证指令发送后才会接受到消息
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String ip = NettyUtils.parseChannelRemoteAddr(ctx.channel());
		//连接数 +1
		ChannelManager.addChannelCount(ctx.channel());
		logger.info(HxgyLog.builder().msg("客户端：{}在线!当前channel数量：{} - 已认证的连接数量：{}").build(),ip,ChannelManager.getChannelCount(),ChannelManager.getAuthUserCount());
	}
	
	/**
	 * channel失去连接时触发
	 *  
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//连接数 -1
		ChannelManager.subChannelCount();
		//处理用户离线
		userService = SpringHelper.getBean("userService");
		userService.handleInactive(ctx.channel());
	}

}
