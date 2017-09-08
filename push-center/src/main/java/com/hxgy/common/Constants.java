/**
 * hxgy Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.hxgy.common;

import com.hxgy.config.Discover;
import com.hxgy.utils.SpringHelper;

/**
 * 常量
 * 
 * @author WindsYan
 * @version $Id: Constants.java, v 0.1 2017年7月27日 下午5:15:18 WindsYan Exp $
 */
public class Constants {
    public static String DEFAULT_HOST = "localhost";
    public static int DEFAULT_PORT = 9688;
    public static String WEBSOCKET_URL = "ws://localhost:8099/websocket";

    // 定义客户端没有收到服务端的pong消息的最大次数
    private static int MAX_UN_REC_PING_TIMES;
    // 检测chanel是否接受过心跳数据
    private static int READ_WAIT_SECONDS;

    private static int MAX_UN_REC_PONG_TIMES;
    private static int WRITE_WAIT_SECONDS;
    private static int RE_CONN_WAIT_SECONDS;
    private static String localIp;
    private static int serverPort;

    static {
        Discover discover = SpringHelper.getBean("discover");
        MAX_UN_REC_PING_TIMES = discover.getHeartBeatConf().getMAX_UN_REC_PING_TIMES();
        READ_WAIT_SECONDS = discover.getHeartBeatConf().getREAD_WAIT_SECONDS();
        MAX_UN_REC_PING_TIMES = discover.getHeartBeatConf().getMAX_UN_REC_PING_TIMES();
        WRITE_WAIT_SECONDS = discover.getHeartBeatConf().getWRITE_WAIT_SECONDS();
        RE_CONN_WAIT_SECONDS = discover.getHeartBeatConf().getRE_CONN_WAIT_SECONDS();
        localIp = discover.getLocalIp();
        serverPort = discover.serverPort;
    }

    public static String getDefaultHost() {
        return DEFAULT_HOST;
    }

    public static int getDefaultPort() {
        return DEFAULT_PORT;
    }

    public static String getWebsocketUrl() {
        return WEBSOCKET_URL;
    }

    public static int getMaxUnRecPingTimes() {
        return MAX_UN_REC_PING_TIMES;
    }

    public static int getReadWaitSeconds() {
        return READ_WAIT_SECONDS;
    }

    public static int getMaxUnRecPongTimes() {
        return MAX_UN_REC_PONG_TIMES;
    }

    public static int getWriteWaitSeconds() {
        return WRITE_WAIT_SECONDS;
    }

    public static int getReConnWaitSeconds() {
        return RE_CONN_WAIT_SECONDS;
    }

    public static String getLocalIp() {
        return localIp;
    }

    public static int getServerPort() {
        return serverPort;
    }
}
