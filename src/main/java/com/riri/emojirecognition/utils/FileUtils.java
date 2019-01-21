package com.riri.emojirecognition.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.io.File;
import java.io.IOException;

/**
 * 文件上传工具包
 */
public class FileUtils {

    @Value("${server.port}")
    private String path;

    /**
     * @param file 文件
     * @param path 文件存放路径
     * @param fileName 源文件名
     * @return
     */
    public static boolean upload(MultipartFile file, String path, String fileName) {

        // 生成新的文件名
        //String realPath = path + "/" + FileUtils.getFileName(fileName);

        //使用原文件名
        String realPath = path + "/" + fileName;

        File dest = new File(realPath);

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
//        int lastIndex = fileName.lastIndexOf(".");
//        if (lastIndex != -1) {
        return fileName.substring(fileName.lastIndexOf("."));
//        }
//        return "";
    }

    /**
     * 生成新的随机文件名
     *
     * @param originalFileName 源文件名
     * @return
     */
    public static String getUUIDFileName(String originalFileName) {
        return UUIDUtils.getUUID() + getSuffix(originalFileName);
    }

    //生成无后缀的源文件名
    public static String getSourceName(String originalFileName) {
//        if (originalFileName != null)
            return originalFileName.substring(0, originalFileName.lastIndexOf("."));
//        else {
//            return "";
//        }
    }
}
