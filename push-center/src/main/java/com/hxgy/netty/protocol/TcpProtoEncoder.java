package com.hxgy.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Tcp的编码handler
 *
 * @author WindsYan
 * @version $Id: com.hxgy.netty.protocol, v 0.1 2017/8/25 17:40 WindsYan Exp $
 */
public class TcpProtoEncoder extends MessageToByteEncoder<TcpProtocol>{
    @Override
    protected void encode(ChannelHandlerContext ctx, TcpProtocol msg, ByteBuf out) throws Exception {
        String body = msg.getBody();
        byte[] bytes = body.getBytes(TcpProtoHeader.CHARSET);
        out.writeShort(TcpProtoHeader.MAGIC)
                .writeByte(msg.getSign())
                .writeByte(msg.getType())
                .writeByte(msg.getStatus())
                .writeInt(bytes.length)
                .writeBytes(bytes);
    }
}
