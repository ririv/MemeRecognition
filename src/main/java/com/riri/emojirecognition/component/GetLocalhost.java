package com.riri.emojirecognition.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class GetLocalhost {

    //获取主机端口
    @Value("${server.port}")
    private static String post;

    //获取本机ip
    private static String host;

    private static final Logger logger = LoggerFactory.getLogger(GetLocalhost.class);

    //获取本机IP
    public static String getLocalAddress() {
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("get server host Exception e:", e);
        }
        return host + ":" + post;
    }

}
