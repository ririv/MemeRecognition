package com.riri.emojirecognition.service;

import com.riri.emojirecognition.component.deeplearning.ClassifierWithDeepLearning4j;
import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.repository.ImgRepository;
import com.riri.emojirecognition.util.FileUtil;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ImgService {
    private static final Logger logger = LoggerFactory.getLogger(ImgService.class);

    @Value("${path.img.base-path}")
    private String imgBasePath;

    @Value("${path.img.admin-path}")
    private String imgAdminPath;

    @Value("${path.img.user-path}")
    private String imgUserPath; // 要上传的目标文件存放路径

    @Value("${classify.proba}")
    private float classifyProba;

    private final ImgRepository imgRepository;
    private ClassifierWithDeepLearning4j classifier;

    @Autowired
    public ImgService(ImgRepository imgRepository, ClassifierWithDeepLearning4j classifier) {
        this.imgRepository = imgRepository;
        this.classifier = classifier;
    }

    public Img save(Img img) {
        return imgRepository.save(img);
    }

    public List<Img> saveAll(List<Img> imgs) {
        return imgRepository.saveAll(imgs);
    }

    public Img findById(Long id) {
        return imgRepository.findById(id).orElse(null);
    }

    public Long findMaxSubIdByTag(String tag){
        return imgRepository.findMaxSubIdByTag(tag);
    }

    public Page<Img> findAll(int page,int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return imgRepository.findAll(pageable);
    }

    public Page<Img> findAll(Pageable pageable){
        return imgRepository.findAll(pageable);
    }


    /**
     * 通过文件夹批量插入到数据库
     * @param targetDirPath 目标文件夹路径
     * @param owner 所有者
     * @param flag 设置为0，则为管理员上传，非0为用户上传，路径会发生相应的改变
     */
    public void batchInsertToDbByDir(String targetDirPath, String owner, int flag) {
        String path; //保存的路径
        //flag 为0 则为管理员上传，不为0则为用户上传
        if (flag == 0) {
            path = imgAdminPath;
            owner = "admin";
        } else {
            path = imgUserPath;
        }

        String parentDirectory = imgBasePath.replace("\\", "/");
        String subdirectory;
        String tag = ""; //初始化为空字符，否则后面第一次将无法赋值给lastTab
        String lastTag; //用lastTab监测tab是否发生了变化
        Long subId = 0L;

        List<FileUtil.FileInfo> imgList = FileUtil.traverseFolder(targetDirPath);

        for (FileUtil.FileInfo fileInfo : imgList) {
            //将所有\改为/，使路径格式统一，方便删除父路径
            subdirectory = fileInfo.getPath().replace("\\", "/");
            //去掉父路径，得到子路径
            subdirectory = subdirectory.replaceAll(parentDirectory, "");

//                //通过最后一个文件夹名存储tag,再去掉"/",此方法逻辑不好
//                tag  = subdirectory
//                        //先去掉最后一个字符"/"
//                        .substring(0,subdirectory.length() - 1)
//                        //再通过获得倒数第二个"/"位置+1来获得最后一个子文件夹名
//                        .substring(subdirectory.lastIndexOf("/")+1);

            //通过"/"切割字符串获得最后一个元素即为最后的子文件夹名做为tag
            String[] subdirArray = subdirectory.split("/");
//                for (String s: subdirArray){System.out.println(s);}//测试用
            //将上一次的tag赋值给lastTag
            lastTag = tag;
            //tag获得新值
            tag = subdirArray[subdirArray.length - 1];

            //检测到tab发生了变化
            if (!tag.equals(lastTag)) {
                subId = imgRepository.findMaxSubIdByTag(tag);
                //当此subId之前没有任何数据时，会产生null值，赋值初始序号为0
                if (subId == null) {
                    subId = 0L;
                }
                //新数据的subId为原数据库中最后条数据的subId+1
                subId += 1;
            } else {
                subId++;
            }

            //new Img()不能放置循环外，否则只会不停更新此img实例，不会增加
            Img img = new Img();
            img.setSourcename(fileInfo.getName());
            img.setSubDir(subdirectory);
//            if(owner!=null){
            img.setOwner(owner);
//            }
            img.setTag(tag);
            img.setSubId(subId);
            imgRepository.save(img);
            System.out.println("文件名：" + img.getSourcename() + "\n" + "子文件夹：" + img.getSubDir()); //测试用
        }
    }

    /**
     * 第一次
     *
     * @param lastTag 上次的tag
     * @param tag 此次的tag
     * @return subId
     */

    public Long subIdCounter(String lastTag, String tag){

        Long subId = 0L;

        //检测到tab发生了变化
        if (!lastTag.equals(tag)) {
            subId = imgRepository.findMaxSubIdByTag(tag);
            //当此tag的subId之前没有任何数据时，会产生null值，赋值初始序号为0
            if (subId == null) {
                subId = 0L;
            }
            //新数据的subId为原数据库中最后条数据的subId+1
            subId++;
        } else {
            subId++;
        }
        return subId;
    }

    /**
     * 如果出现相同的图片，可能是因为两张图片本身就相同或相似
     * 或者数据库有两个sourcename（文件名）有相同的值，请删除
     *
     * 第一种方法，生成随机数，通过subId依次查询
     * 优点，满足要求，且避免了查询为null的空数据
     * 缺点：在subId不连续的情况下会进行多次查询，直到满足数量。单个查询，增加了数据库的压力
     * 适合一定要满足数量要求时使用
     */
    public List<Img> findRandomImgsByTagLimitNum(String tag, int num) {
        Img img;

        List<Long> randomNumList = new ArrayList<>(); //避免随机数的重复，与重复查询
        List<Img> imgs = new ArrayList<>(); //用来存储Img

        Long max = imgRepository.findMaxSubIdByTag(tag);
        Long min = imgRepository.findMinSubIdByTag(tag);
        long count = imgRepository.countByTag(tag);

        long randomNum;

        //如果num小于数据库中该tag的总数目count，则将count赋值给num
        if (count < num) {
            num = (int) count;
        }

        while (imgs.size() < num) {        //当所要的img数量不满足要求时，则循环
            randomNum = ThreadLocalRandom.current().nextLong(min, max + 1); //创建Long类型随机数，范围为min~max
            if (!randomNumList.contains(randomNum)) { //当randomNumList中不包含此随机数
                img = imgRepository.findByTagAndSubId(tag, randomNum); //查询此img
                if (img!=null) {
                    imgs.add(img);
                }
                randomNumList.add(randomNum); //将此随机数添加至列表，避免重复查询
            }
        }
        return imgs;
    }

    /**
     *  第二种方法，生成随机数，通过subId查询
     *  优点：一次性查询完毕，数据库压力小
     *  缺点：在subId不连续的情况下，可能会查询到空数据，虽然最终结果里空数据被省去，不会有null值，但是数据的数量要求不满足
     *  适合subId连续时使用
     */
    public List<Img> findRandomImgsByTagLimitNum2(String tag, int num) {
        Long max = imgRepository.findMaxSubIdByTag(tag);
        Long min = imgRepository.findMinSubIdByTag(tag);
        long count = imgRepository.countByTag(tag);
        long randomNum;
        Set<Long> randomNumSet = new HashSet<>(); //定义Set，避免随机数的重复

        if (count < num) {
            num = (int) count;
        }

        while (randomNumSet.size() < num) {
            randomNum = ThreadLocalRandom.current().nextLong(min, max + 1);
            randomNumSet.add(randomNum);
        }
        System.out.println((randomNumSet));
        return imgRepository.findByTagAndSubIdIn(tag,randomNumSet);
    }

    /**
     * 第三种方法，从数据库，取出全部符合tag标签的数据，从中随机抽取数据
     * 优点：一次性查询完毕，数据库压力小
     * 缺点，数据量太大时，list在内存中存储的数据太大
     * 适合数据量不是异常大时使用
     */
    public List<Img> findRandomImgsByTagLimitNum3(String tag, int num) {
        List<Img> imgs = imgRepository.findByTag(tag);

        List<Long> subIds = new ArrayList<>();
        List<Img> results = new ArrayList<>();
        Set<Integer> randomNumSet = new HashSet<>(); //定义Set，避免随机数的重复
        long count = imgRepository.countByTag(tag);
        int randomNum;

        for (Img img: imgs) {
            subIds.add(img.getSubId());
        }

        if (count < num) {
            num = (int) count;
        }

        while (randomNumSet.size() < num) {
            //生成随机数，范围subId的索引范围
            //因为生成的随机数是左闭右开，需要+1，而subId.size需要-1，正好抵消
            randomNum = ThreadLocalRandom.current().nextInt(0, subIds.size());
            randomNumSet.add(randomNum);
        }
        for (int id:randomNumSet) {
            results.add(imgs.get(id));
        }

        return results;
    }

    public Optional<String> classify(File image) {

        Pair<String, Float> labelWithProba;
        String label = null;
        Float proba;
        try {
            labelWithProba = classifier.classify(image);
            label = labelWithProba.getKey();
            proba = labelWithProba.getValue();
            logger.info("The predict label:" + label);
            logger.info("The predict proba:" + proba.toString());
            if (proba < classifyProba) {
                label = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(label);
    }
}
