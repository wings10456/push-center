/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.utils;

import java.net.SocketAddress;

import com.hxgy.config.Discover;
import io.netty.channel.Channel;

/**
 * 
 * @author WindsYan
 * @version $Id: NettyUtils.java, v 0.1 2017年7月27日 下午5:20:46 WindsYan Exp $
 */
public class NettyUtils {

    /**
     * 获取Channel的远程IP地址
     * @param channel
     * @return
     */
    public static String parseChannelRemoteAddr(final Channel channel) {
        if (null == channel) {
            return "";
        }
        SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (addr.length() > 0) {
            int index = addr.lastIndexOf("/");
            if (index >= 0) {
                return addr.substring(index + 1);
            }

            return addr;
        }

        return "";
    }
    public static String getHost(final Channel channel){
        String address[] = parseChannelRemoteAddr(channel).split(":");
        if(address.length <= 0){
            return "";
        }
        return address[0];
    }
    public static String getPort(final Channel channel){
        String address[] = parseChannelRemoteAddr(channel).split(":");
        if(address.length <= 0){
            return "";
        }
        return address[1];
    }

    public static String getLocalAddress(){
        Discover discover = SpringHelper.getBean("discover");
        return discover.getLocalIp();
    }
}
