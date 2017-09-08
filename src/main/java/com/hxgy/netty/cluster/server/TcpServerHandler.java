package com.hxgy.netty.cluster.server;

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
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TcpServer服务器处理的handler
 *
 * @author WindsYan
 * @version $Id: com.hxgy.netty.cluster.server, v 0.1 2017/8/24 16:30 WindsYan Exp $
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<String> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // 失败计数器：未收到client端发送的ping请求
    private int unRecPingTimes = 0 ;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        String host = NettyUtils.getHost(ctx.channel());
        logger.info(HxgyLog.builder().user(host).method("channelRead0").msg("收到一条消息：{}").build(),s);
        Proto proto = Proto.fromJson(s);
        if(proto != null){
            if(proto.getType() != 0){
                switch (proto.getType()){
                    case Proto.Type.HEART_BEAT_PING :
                        //收到一个PING心跳，立即回发一个PONG心跳
                        ctx.channel().writeAndFlush(JSON.toJSONString(Proto.creatHeartBeatPong()));
                        //心跳失败计数器清零
                        unRecPingTimes = 0;
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
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                //读超时
                logger.info(HxgyLog.builder().method("userEventTriggered").msg("服务器读超时！！").build());
                // 失败计数器次数大于等于3次的时候，关闭链接，等待client重连
                if(unRecPingTimes >= Constants.getMaxUnRecPingTimes()){
                    logger.info(HxgyLog.builder().method("userEventTriggered").msg("读超时，关闭chanel！").build());
                    // 连续超过3次未收到client的ping消息，那么关闭该通道，等待client重连
                    ctx.channel().close();
                }else{
                    // 失败计数器加1
                    unRecPingTimes++;
                }
            } else if (event.state() == IdleState.WRITER_IDLE) {
                //写超时
                logger.info(HxgyLog.builder().method("userEventTriggered").msg("服务器写超时！！").build());
            } else if (event.state() == IdleState.ALL_IDLE) {
                //超时
                logger.info(HxgyLog.builder().method("userEventTriggered").msg("服务器读写超时！！").build());
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String host = NettyUtils.getHost(ctx.channel());
        ClusterChannelManager.addChannel(host,ctx.channel());
        logger.info(HxgyLog.builder().user(host).method("channelActive").msg("host:{}成功连接，保存连接至本地内存").build(),host);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String host = NettyUtils.getHost(ctx.channel());
        //删除集群通道缓存（ip -> channel）
        ClusterChannelManager.removeChannel(host);
        //删除此channel对应服务器，在本机缓存中的所有路由信息
        RoutingManager.removeHostRouting(host);
        logger.info(HxgyLog.builder().user(host).method("channelInactive").msg("host:{}失去连接！！").build(),host);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String host = NettyUtils.getHost(ctx.channel());
        logger.error(HxgyLog.builder().method("exceptionCaught").msg("host：{} 发生异常！").build(),host,cause);
        ctx.close();
    }
}
