package com.hxgy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author WindsYan
 * @version $Id: com.hxgy.config, v 0.1 2017/8/25 15:13 WindsYan Exp $
 */
@ConfigurationProperties(prefix = "discover")
@Component
public class Discover {

    @Value("${server.port}")
    public int serverPort;

    private String localIp;
    private int localTcpPort;
    private List<Nodes> nodes;
    private HeartBeatConf heartBeatConf;

    public static class Nodes{
        private String host;
        private int port;
        private String status;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class HeartBeatConf{
        private int MAX_UN_REC_PING_TIMES;
        private int READ_WAIT_SECONDS;
        private int MAX_UN_REC_PONG_TIMES;
        private int WRITE_WAIT_SECONDS;
        private int RE_CONN_WAIT_SECONDS;

        public int getMAX_UN_REC_PING_TIMES() {
            return MAX_UN_REC_PING_TIMES;
        }

        public void setMAX_UN_REC_PING_TIMES(int MAX_UN_REC_PING_TIMES) {
            this.MAX_UN_REC_PING_TIMES = MAX_UN_REC_PING_TIMES;
        }

        public int getREAD_WAIT_SECONDS() {
            return READ_WAIT_SECONDS;
        }

        public void setREAD_WAIT_SECONDS(int READ_WAIT_SECONDS) {
            this.READ_WAIT_SECONDS = READ_WAIT_SECONDS;
        }

        public int getMAX_UN_REC_PONG_TIMES() {
            return MAX_UN_REC_PONG_TIMES;
        }

        public void setMAX_UN_REC_PONG_TIMES(int MAX_UN_REC_PONG_TIMES) {
            this.MAX_UN_REC_PONG_TIMES = MAX_UN_REC_PONG_TIMES;
        }

        public int getWRITE_WAIT_SECONDS() {
            return WRITE_WAIT_SECONDS;
        }

        public void setWRITE_WAIT_SECONDS(int WRITE_WAIT_SECONDS) {
            this.WRITE_WAIT_SECONDS = WRITE_WAIT_SECONDS;
        }

        public int getRE_CONN_WAIT_SECONDS() {
            return RE_CONN_WAIT_SECONDS;
        }

        public void setRE_CONN_WAIT_SECONDS(int RE_CONN_WAIT_SECONDS) {
            this.RE_CONN_WAIT_SECONDS = RE_CONN_WAIT_SECONDS;
        }
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public List<Nodes> getNodes() {
        return nodes;
    }

    public void setNodes(List<Nodes> nodes) {
        this.nodes = nodes;
    }

    public HeartBeatConf getHeartBeatConf() {
        return heartBeatConf;
    }

    public void setHeartBeatConf(HeartBeatConf heartBeatConf) {
        this.heartBeatConf = heartBeatConf;
    }

    public int getLocalTcpPort() {
        return localTcpPort;
    }

    public void setLocalTcpPort(int localTcpPort) {
        this.localTcpPort = localTcpPort;
    }
}
