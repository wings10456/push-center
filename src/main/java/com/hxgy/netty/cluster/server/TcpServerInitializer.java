package com.hxgy.netty.cluster.server;

import com.hxgy.HxgyLog;
import com.hxgy.common.Constants;
import com.hxgy.netty.protocol.TcpProtoDecoder;
import com.hxgy.netty.protocol.TcpProtoEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author WindsYan
 * @version $Id: com.hxgy.netty.cluster.server, v 0.1 2017/8/24 16:30 WindsYan Exp $
 */
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("pong",new IdleStateHandler(Constants.getReadWaitSeconds(),0,0));
        pipeline.addLast("handler", new TcpServerHandler());

        logger.info(HxgyLog.builder().method("ToDelete").msg("初始化channel通道,{}").build(),ch.remoteAddress());
    }
}
