package com.riri.emojirecognition.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具包
 */
public class FileUtil {

    @Value("${server.port}")
    private String port;

    public static class FileInfo{
        private String name;
        private String path;

       private FileInfo(String name, String path) {
            this.name = name;
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    /**
     * @param file 文件
     * @param path 文件存放路径
     * @param fileName 源文件名
     * @return
     */
    public static boolean upload(MultipartFile file, String path, String fileName) {

        // 生成新的文件名
//        String newPath = path + "/" + getUUIDFileName(fileName);

        //使用原文件名
        String realPath = path + "/" + fileName;

        File dest = new File(realPath);

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
//            System.out.println(dest.getParentFile().mkdirs());
            boolean dirCreated = dest.getParentFile().mkdirs();
            if(dirCreated) {System.out.println("Directory was created");}
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
     * @param filename 文件名
     * @return
     */
    public static String getSuffix(String filename) {
//        int lastIndex = fileName.lastIndexOf(".");
//        if (lastIndex != -1) {
        return filename.substring(filename.lastIndexOf("."));
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
        return UUIDUtil.getUUID() + getSuffix(originalFileName);
    }

    //生成无后缀的源文件名
    public static String getSourceName(String originalFileName) {
//        if (originalFileName != null) {
            return originalFileName.substring(0, originalFileName.lastIndexOf("."));
//        }
//        else {
//            return "";
//        }
    }

    @Value("${web.default-path}")
    private static String defaultPath;

    //遍历文件夹
    public static List<FileInfo> traverseFolder(String dirpath){

        File file = new File(dirpath);
        List<FileInfo> fileList = new ArrayList<>();

        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
            }
            else {
                for (File f : files) {
                    if (f.isDirectory()) {
                        System.out.println("文件夹: " + f.getAbsolutePath());
                        traverseFolder(f.getAbsolutePath());
                    }
                    else {
                        System.out.println("文件: " + f.getAbsolutePath());
                            //获得文件信息并保存至数组

                        FileInfo fileInfo = new FileInfo(f.getName(), (f.getParent()+"\\"));
                        System.out.println("文件名: "+f.getName()+"\n"+"父路径: "+ f.getParent()+"\\");
                        fileList.add(fileInfo);
                        }
                    }
                }
            }
        else {
            System.out.println("文件不存在!");

        }
        return fileList;
    }
}
