/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.netty.cluster.client;

import com.hxgy.common.Constants;
import com.hxgy.netty.protocol.TcpProtoDecoder;
import com.hxgy.netty.protocol.TcpProtoEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 
 * @author WindsYan
 * @version $Id: TcpClientInitializer.java, v 0.1 2017年8月24日 上午11:04:18 WindsYan Exp $
 */
public class TcpClientInitializer extends ChannelInitializer<SocketChannel> {

    private TcpClient client;
    public TcpClientInitializer(TcpClient client){
        this.client = client;
    }

	@Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

//        pipeline.addLast("decoder", new TcpProtoDecoder());
//        pipeline.addLast("encoder", new TcpProtoEncoder());
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        //第一个参数是指定读操作空闲秒数，第二个参数是指定写操作的空闲秒数，第三个参数是指定读写空闲秒数
        //当有操作操作超出指定空闲秒数时，便会触发UserEventTriggered事件。
        pipeline.addLast(new IdleStateHandler(0, Constants.getWriteWaitSeconds(), 0));
        pipeline.addLast(new TcpClientHandler(client));
    }
}
