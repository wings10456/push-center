package com.hxgy.netty.cluster.server;

import com.hxgy.HxgyLog;
import com.hxgy.common.BaseTcpServer;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 用于集群消息传递的TcpServer
 *
 * @author WindsYan
 * @version $Id: com.hxgy.netty.cluster.server, v 0.1 2017/8/24 16:20 WindsYan Exp $
 */
public class TcpServer extends BaseTcpServer{

    private int port;

    public TcpServer(int port) {
        this.port = port;
    }

    public void start(){
        try {
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new TcpServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定端口，开始接收进来的连接
            channelFuture = serverBootstrap.bind(port).sync(); // (7)

            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    logger.info(HxgyLog.builder().msg("TcpServer启动成功！端口:{}").build(),port);
                } else {
                    logger.error(HxgyLog.builder().msg("TcpServer启动失败！端口:{}").build(),port);
                }
            });

        }catch (Exception e){
            logger.error(HxgyLog.builder().msg("TcpServer启动失败！端口:{}").build(),port);
            shutdown();
        }
    }
}
