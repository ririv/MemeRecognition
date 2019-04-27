package com.riri.emojirecognition.util;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 文件工具包
 */
public class FileUtil {

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
     * @param mFile 文件
     * @param path 文件存放路径
     * @return
     */
    public static File upload(MultipartFile mFile, String path) {

        File dest = new File(path);

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
//            System.out.println(dest.getParentFile().mkdirs());
            boolean dirCreated = dest.getParentFile().mkdirs();
            if(dirCreated) {System.out.println("Directory was created");}
        }

        try {
            //保存文件
//            FileUtils.copyInputStreamToFile(mFile.getInputStream(), dest);
            mFile.transferTo(dest);

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return dest;
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
    public static String getUUIDFilename(String originalFileName) {
        //如果要去除-
        // UUID.randomUUID().toString().replace("-", "");
        return UUID.randomUUID() + getSuffix(originalFileName);
    }

    //生成无后缀的源文件名
    public static String getFilenameWithoutExt(String originalFileName) {
//        if (originalFileName != null) {
            return originalFileName.substring(0, originalFileName.lastIndexOf("."));
//        }
//        else {
//            return "";
//        }
    }

    //遍历文件夹，迭代方式
    public static List<FileInfo> traverseFolder(String dirPath) {
        int fileNum = 0, folderNum = 0;
        File file = new File(dirPath);
        List<FileInfo> fileList = new ArrayList<>();

        if (file.exists()) {
            //创建文件夹列表,这里涉及到删除第一个元素，所以使用LinkedList
            LinkedList<File> folderList = new LinkedList<>();
            //获取该文件夹下的所有文件
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
            } else {
                //先得到所有文件夹包括子文件夹的列表
                for (File f : files) {
                    if (f.isDirectory()) {
                        System.out.println("文件夹:" + f.getAbsolutePath());
                        folderList.add(f);
                        folderNum++;

                    } else {
                        System.out.println("文件:" + f.getAbsolutePath());
                        fileNum++;

                        //获得文件信息并保存至数组
                        FileInfo fileInfo = new FileInfo(f.getName(), f.getParent()+"\\");
                        System.out.println("文件名: "+f.getName());
                        System.out.println("父路径: "+ f.getParent()+"\\");
                        fileList.add(fileInfo);
                    }
                }
                //再遍历得到的文件夹列表下的所有文件夹
                File folder;
                while (!folderList.isEmpty()) {
                    //移除已遍历的文件夹，用while循环直至未遍历的文件夹列表为空
                    //folder为当前所移除的文件夹
                    folder = folderList.removeFirst();
                    //获取该文件夹下的所有文件
                    files = folder.listFiles();
                    if (null == files || files.length == 0) {
                        System.out.println("该子文件夹是空的: "+file.getName());
                    } else {
                        for (File f : files) {
                            if (f.isDirectory()) {
                                System.out.println("文件夹:" + f.getAbsolutePath());
                                //检测到为文件夹，添加到文件夹列表
                                folderList.add(f);
                                folderNum++;
                            } else {
                                System.out.println("文件:" + f.getAbsolutePath());
                                fileNum++;

                                //获得文件信息并保存至数组
                                FileInfo fileInfo = new FileInfo(f.getName(), f.getParent() + "\\");
                                System.out.println("文件名: " + f.getName());
                                System.out.println("父路径: " + f.getParent() + "\\");
                                fileList.add(fileInfo);
                            }
                        }
                    }
                }
            }
        } else{ System.out.println("文件不存在!");
        }
        System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);
        return fileList;
    }

//    //遍历文件夹，递归方式
    public static List<FileInfo> traverseFolder2(String dirPath){

        File file = new File(dirPath);
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
                        traverseFolder2(f.getAbsolutePath());
                    }
                    else {
                        System.out.println("文件: " + f.getAbsolutePath());

                        //获得文件信息并保存至数组
                        FileInfo fileInfo = new FileInfo(f.getName(), f.getParent()+"\\");
                        System.out.println("文件名: "+f.getName());
                        System.out.println("父路径: "+ f.getParent()+"\\");
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
