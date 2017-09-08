/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.netty.cluster.client;

import com.alibaba.fastjson.JSON;
import com.hxgy.HxgyLog;
import com.hxgy.common.Constants;
import com.hxgy.domain.ChatMessage;
import com.hxgy.domain.Proto;
import com.hxgy.domain.RoutingMessage;
import com.hxgy.manager.ChannelManager;
import com.hxgy.manager.ClusterChannelManager;
import com.hxgy.manager.RoutingManager;
import com.hxgy.utils.NettyUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Tcp客户端处理的handler
 * 
 * @author WindsYan
 * @version $Id: TcpClientHandler.java, v 0.1 2017年8月24日 上午11:06:03 WindsYan Exp $
 */
public class TcpClientHandler extends SimpleChannelInboundHandler<String>{

    private TcpClient client;
    TcpClientHandler(TcpClient client) {
        this.client = client;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    /** 未收到pong消息的计数器 */
    private int unRecPongTimes = 0;

	/** 
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String host = NettyUtils.getHost(ctx.channel());
        logger.info(HxgyLog.builder().method("channelRead0").user(host).msg("host:{} msg: {}").build(),host,msg);

        Proto proto = Proto.fromJson(msg);
        if(proto != null){
            if(proto.getType() != 0){
                switch (proto.getType()){
                    case Proto.Type.HEART_BEAT_PONG :
                        //收到一个PONG心跳返回，计数器清零
                        unRecPongTimes = 0;
                        break;
                    case Proto.Type.ROUTING :
                        //收到一个路由消息，用户上线消息，将用户的路由存入本地缓存
                        RoutingMessage routing = JSON.parseObject(proto.getData().toString(),RoutingMessage.class);
                        if(routing != null){
                            RoutingManager.addRouting(routing.getUserId(),routing.getServerIp());
                        }
                        break;
                    case Proto.Type.ROUTING_DELETE :
                        //收到一个删除路由消息，移除本机缓存的此路由关系
                        RoutingMessage routing0 = JSON.parseObject(proto.getData().toString(),RoutingMessage.class);
                        if(routing0 != null){
                            RoutingManager.removeRouting(routing0.getUserId());
                        }
                        break;
                    case Proto.Type.CHAT:
                        //收到一个需要转发的聊天消息，转发消息
                        ChatMessage chatMessage = JSON.parseObject(proto.getData().toString(),ChatMessage.class);
                        if(chatMessage != null){
                            ChannelManager.sendLocalChatMessage(chatMessage);
                        }
                        break;
                }
            }else {
                logger.info(HxgyLog.builder().user(host).method("channelRead0").msg("未检测到正确的消息类型").build());
            }
        }else {
            logger.info(HxgyLog.builder().user(host).method("channelRead0").msg("协议解析失败！").build());
        }
	}

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String host = NettyUtils.getHost(ctx.channel());
        logger.info(HxgyLog.builder().method("channelInactive").user(host).msg("host:{}通道Inactive！").build(),host);
        //删除集群通道缓存（ip -> channel）
        ClusterChannelManager.removeChannel(host);
        //删除此channel对应服务器，在本机缓存中的所有路由信息
        RoutingManager.removeHostRouting(host);

        //延迟5秒，断线重连
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                client.reConnect();
            }
        }, Constants.getReConnWaitSeconds(), TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String host = NettyUtils.getHost(ctx.channel());
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    logger.info(HxgyLog.builder().method("心跳检测").user(host).msg("客户端 READER_IDLE读超时").build(),host);
                    break;
                case WRITER_IDLE: //5s没有写操作会发送心跳一次
//                    int times = Constants.MAX_UN_REC_PING_TIMES;
                    if(unRecPongTimes < Constants.getMaxUnRecPingTimes()){
                        ctx.writeAndFlush(JSON.toJSONString(Proto.creatHeartBeatPing()));
                        logger.info(HxgyLog.builder().method("心跳检测").user(host).msg("发送心跳检测信息到【{}】").build(),host);
                        //没收到pong的次数+1
                        unRecPongTimes++;
                    }else{
                        //超过最大次数没有收到pong，断开连接
                        ctx.channel().close();
                    }
                    break;
                case ALL_IDLE:
                    logger.info(HxgyLog.builder().method("心跳检测").user(host).msg("客户端 ALL_IDLE").build(),host);
                    break;
                default:
                    break;
            }
        }
    }
}
