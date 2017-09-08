package com.hxgy.netty.protocol;

/**
 * 通信协议实体
 *
 * @author WindsYan
 * @version $Id: com.hxgy.netty.protocol, v 0.1 2017/8/25 17:29 WindsYan Exp $
 */
public class TcpProtocol {

    // 消息标志
    private byte sign;
    // 消息类型
    private byte type;
    // 响应状态
    private byte status;
    // 消息体
    private String body;

    public TcpProtocol(byte sign, byte type, byte status) {
        this.sign = sign;
        this.type = type;
        this.status = status;
    }

    public TcpProtocol() {
    }

    public byte getSign() {
        return sign;
    }

    public void setSign(byte sign) {
        this.sign = sign;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
