package com.hxgy.netty.protocol;

import com.hxgy.HxgyLog;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * TCP协议解码器
 *
 * @author WindsYan
 * @version $Id: com.hxgy.netty.protocol, v 0.1 2017/8/25 17:40 WindsYan Exp $
 */
public class TcpProtoDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(TcpProtoDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        in.markReaderIndex();

        if (in.readShort() != TcpProtoHeader.MAGIC) {
            // Magic不一致，表明不是自己的数据
            logger.warn(HxgyLog.builder().method("TcpProtoDecoder解码器").msg("解码失败！Magic不一致").build());
            in.resetReaderIndex();
            return;
        }

        // 开始解码
        byte sign = in.readByte();
        byte type = in.readByte();
        byte status = in.readByte();

        // 确认消息体长度
        int bodyLength = in.readInt();
        if (in.readableBytes() != bodyLength) {
            // 消息体长度不一致
            logger.warn(HxgyLog.builder().method("TcpProtoDecoder解码器").msg("解码失败！消息体长度不一致！").build());
            in.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[bodyLength];
        in.readBytes(bytes);

        TcpProtocol tcpProtocol = new TcpProtocol();
        tcpProtocol.setSign(sign);
        tcpProtocol.setType(type);
        tcpProtocol.setStatus(status);
        tcpProtocol.setBody(new String(bytes, TcpProtoHeader.CHARSET));

        out.add(tcpProtocol);
    }
}
