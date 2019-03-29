package com.riri.emojirecognition.service;

import com.riri.emojirecognition.component.deeplearning.ClassifierWithDeepLearning4j;
import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.repository.ImgRepository;
import com.riri.emojirecognition.util.FileUtil;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

@Service
public class ImgService {

    @Value("${web.default-path}")
    private String defaultPath;

    @Value("${classify.proba}")
    private float classifyProba;

    private final ImgRepository imgRepository;
    private ClassifierWithDeepLearning4j classifier;

    @Autowired
    public ImgService(ImgRepository imgRepository, ClassifierWithDeepLearning4j classifier ) {
        this.imgRepository = imgRepository;
        this.classifier = classifier;
    }

    public Img save(Img img) {
        return imgRepository.save(img);
    }

    public List<Img> saveAll(List<Img> imgs){
        return imgRepository.saveAll(imgs);
    }


    public Img findById(Long id) {
        return imgRepository.findById(id).orElse(null);
    }

    public void batchInsertToDbByFolder(String dirpath, String owner) {
        String parentdirectory = defaultPath.replace("\\", "/");
        String subdirectory;
        String tag = ""; //初始化为空字符，否则后面第一次将无法赋值给lastTab
        String lastTag; //用lastTab监测tab是否发生了变化
        Long subId = 0L;

        List<FileUtil.FileInfo> imgList = FileUtil.traverseFolder(dirpath);

        for (FileUtil.FileInfo fi : imgList) {
            //将所有\改为/，使格式统一化，方便删除父路径
            subdirectory = fi.getPath().replace("\\", "/");
            //去掉父路径
            subdirectory = subdirectory.replaceAll(parentdirectory, "");

//                //通过最后一个文件夹名存储tab,再去掉"/",此方法逻辑不好
//                tab  = subdirectory
//                        //先去掉最后一个字符"/"
//                        .substring(0,subdirectory.length() - 1)
//                        //再通过获得倒数第二个"/"位置+1来获得最后一个子文件夹名
//                        .substring(subdirectory.lastIndexOf("/")+1);

            //通过"/"切割字符串获得最后一个元素即为最后的子文件夹名做为tab
            String[] subdirArray = subdirectory.split("/");
//                for (String s: subdirArray){System.out.println(s);}//测试用
            //将上一次的tab赋值给lastTab
            lastTag = tag;
            //tab获得新值
            tag = subdirArray[subdirArray.length - 1];
            //检测到tab发生了变化
            if (!lastTag.equals(tag)){
                subId = imgRepository.findMaxSubIdByTag(tag);
                //当subId之前没有任何元素使，会产生null值，为避免空指针异常，进行初始化
                if (subId == null){
                    subId = 0L;
                }
                //新数据的subId从原数据库中最后一个subId+1开始
                subId += 1;
            }
            else {subId++;}

            //new Img()不能放置循环外，否则只会不停更新一
            // 个img，不会增加
            Img img = new Img();
            img.setName(fi.getName());
            img.setSubDir(subdirectory);
//                if(owner!=null){
            img.setOwner(owner);
//                }
            img.setTag(tag);
            img.setSubId(subId);
            imgRepository.save(img);
            System.out.println("图片名：" + img.getName() + "\n" + "子文件夹：" + img.getSubDir()); //测试用
        }
    }

    public List<Img> findRandomImgsByTagLimitNum(String tag,Integer num){
//        return imgRepository.findRandomImgsByTagLimitNum(tag, num);
        Img img;
        //经权衡与测试，使用hashmap可以有效使图片数达到指定数量，并且不重复，key为id，value为img
        Map<Long,Img> idImgMap = new HashMap<>();

        Long max = imgRepository.findMaxSubIdByTag(tag);
        Long min = imgRepository.findMinSubIdByTag(tag);
        long count = imgRepository.countByTag(tag);

        long randomNum;

        //如果查找的数据库的
        if (count<num) {
            num = (int)count;
        }

        while (idImgMap.size() < num){
            randomNum = ThreadLocalRandom.current().nextLong(min, max + 1);

//            //在数据量较少时，随机数可能发生重合，但在数据量大时，此几率可以忽略不计
//            //主要时考虑到数据库中可能发生跨id存储数据的现象,所以必须得检测该id的数据是否存在
//            //而如果先将一组随机数全部生成好，再进行数据库的查询，虽能可以避免随机数的重叠
//            //但不能避免是否会有不存在此id的数据
//            img = imgRepository.findBySubIdAndTag(randomNum,tag);
//            if (img != null){
//                idImgMap.put(randomNum,img);
//            }

            //第二种，此方法虽然可以避免重复查询，但是操作繁琐
            idImgMap.put(randomNum,null);

        }

        //第二种续
        for (long rn: idImgMap.keySet()) {
            img = imgRepository.findBySubIdAndTag(rn,tag);
            if (img != null){
                idImgMap.put(rn,img);
            }

            System.out.println(rn);//TODO 测试
        }

        return new ArrayList<>(idImgMap.values());
    }

    public String classify(File image){

        Pair<String,Float> labelwithProba ;
        String label = null;
        Float proba;
        try {
           labelwithProba =  classifier.classify(image);
           proba = labelwithProba.getValue();
           if (proba > classifyProba) {
           label = labelwithProba.getKey();
           }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return label;
    }
}
