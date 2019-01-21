package com.riri.emojirecognition.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalAddressUtils {

    //获取主机端口
    @Value("${server.port}")
    private static String post;

    //获取本机ip
    private static String host;

    private static final Logger logger = LoggerFactory.getLogger(LocalAddressUtils.class);

    //获取本机IP
    public static String getLocalAddress() {
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("get server host Exception e:", e);
        }
        //        System.out.println("本机地址:"+LocalAddressUtils.getLocalAddress());
        return host + ":" + post;
    }

}
