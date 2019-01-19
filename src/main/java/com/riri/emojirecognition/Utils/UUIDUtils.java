package com.riri.emojirecognition.Utils;

import java.util.UUID;

/**
 * 生成随机UUID工具类
 */
public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}
