package com.riri.emojirecognition.Utils;

public class FileNameUtils {

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
//        if (lastIndex != -1) {
            return fileName.substring(fileName.lastIndexOf("."));
//        }
//        return "";
    }

    /**
     * 生成新的随机文件名
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getUUIDFileName(String fileOriginName){
        return UUIDUtils.getUUID() + FileNameUtils.getSuffix(fileOriginName);
    }

}
