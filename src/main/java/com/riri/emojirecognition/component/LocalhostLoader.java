package com.riri.emojirecognition.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class LocalhostLoader {

    //获取主机端口
    @Value("${server.port}")
    private String post;

    //获取本机ip
    private String host;

    private static final Logger logger = LoggerFactory.getLogger(LocalhostLoader.class);

    //获取本机IP
    public String getLocalAddress() {
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("get server host Exception:", e);
        }
        return host + ":" + post;
    }

}
