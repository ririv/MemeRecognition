package com.riri.emojirecognition.util;

import java.util.UUID;

/**
 * 生成随机UUID工具类
 */
public class UUIDUtil {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}
