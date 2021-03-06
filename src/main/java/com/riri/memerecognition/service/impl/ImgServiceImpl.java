package com.riri.memerecognition.service.impl;

import com.riri.memerecognition.domain.Img;
import com.riri.memerecognition.exception.notfound.ImgNotFoundException;
import com.riri.memerecognition.repository.ImgRepository;
import com.riri.memerecognition.service.ImgService;
import com.riri.memerecognition.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ImgServiceImpl implements ImgService {

    @Value("${path.base.img}")
    private String imgBasePath;

    private final ImgRepository imgRepository;

    @Autowired
    public ImgServiceImpl(ImgRepository imgRepository) {
        this.imgRepository = imgRepository;
    }


    public Img save(Img img) {
        return imgRepository.save(img);
    }


    public List<Img> saveAll(List<Img> imgs) {
        return imgRepository.saveAll(imgs);
    }


    public Img findById(Long id) {
        Optional<Img> img = imgRepository.findById(id);
        if (!img.isPresent()) {
            throw new ImgNotFoundException("The img is not found, img id: " + id);
        }
        return img.get();
    }


    public void deleteById(Long id) {
        imgRepository.deleteById(id);
    }


    public Long findMaxSubIdByTag(String tag) {
        return imgRepository.findMaxSubIdByTag(tag);
    }


    public Page<Img> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return imgRepository.findAll(pageable);
    }


    public Page<Img> findAll(int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        return imgRepository.findAll(pageable);
    }


    public Page<Img> findAll(int page, int size, Sort.Direction direction, String... properties) {
        Pageable pageable = PageRequest.of(page, size, direction, properties);
        return imgRepository.findAll(pageable);
    }


    public Page<Img> findAll(Pageable pageable) {
        return imgRepository.findAll(pageable);
    }


    public Page<Img> findByTag(String tag, Pageable pageable) {
        return imgRepository.findByTag(tag, pageable);
    }


    public Img updateById(Long id, Img img) {
        findById(id);//如果id不存在则会报出异常
        //设置id为所指定的id，防止user中有id信息，而发生更新错位的现象
        img.setId(id);

        return imgRepository.save(img);
    }


    public Img add(Img img) {
        img.setId(null);
        return imgRepository.save(img);
    }

    public void enableImgById(Long id) {
        Img img = findById(id);//如果id不存在则会报出异常
        enableImg(img);
    }

    public void enableImg(Img img) {
        if (img.getTag() != null) {
            img.setSubId(generateSubId(img.getTag()));
            imgRepository.save(img);
        } else throw new RuntimeException("Tag is null");
    }

    public long generateSubId(String tag) {
        Long subId = imgRepository.findMaxSubIdByTag(tag);

        if (subId == null) {
            subId = 0L;
        }
        //新数据的subId为原数据库中最后条数据的subId+1
        //subId从1开始，而不是0

        return subId + 1;
    }


    /**
     * 通过文件夹批量插入到数据库
     * 管理员初始化数据库时使用
     * 默认拥有者为admin，且图片设为启用enabled=true
     *
     * @param targetDirPath 目标文件夹路径
     */
    public void batchInsertToDbByDir(String targetDirPath) {
        String owner = "admin";

        String parentDirectory = imgBasePath.replace("\\", "/");
        String subdirectory;
        String tag = ""; //初始化为空字符，否则后面第一次将无法赋值给lastTab
        String lastTag; //用lastTab监测tab是否发生了变化
        long subId = 0L;

        List<FileUtil.FileInfo> imgList = FileUtil.traverseFolder(targetDirPath);

        for (FileUtil.FileInfo fileInfo : imgList) {
            //将所有\改为/，使路径格式统一，方便删除父路径
            subdirectory = fileInfo.getParentPath().replace("\\", "/");
            //去掉父路径，得到子路径
            subdirectory = subdirectory.replaceAll(parentDirectory, "");

//                //通过最后一个文件夹名存储tag,再去掉"/",此方法逻辑不好
//                tag  = subdirectory
//                        //先去掉最后一个字符"/"
//                        .substring(0,subdirectory.length() - 1)
//                        //再通过获得倒数第二个"/"位置+1来获得最后一个子文件夹名
//                        .substring(subdirectory.lastIndexOf("/")+1);

            //通过"/"切割字符串获得最后一个元素即为最后的子文件夹名做为tag
            String[] subDirArray = subdirectory.split("/");
//                for (String s: subDirArray){System.out.println(s);}//测试用
            //将上一次的tag赋值给lastTag
            lastTag = tag;
            //tag获得新值
            tag = subDirArray[subDirArray.length - 1];

            //检测到tab发生了变化
            if (!tag.equals(lastTag)) {
                subId = generateSubId(tag);
            } else { //如果两次tag一样，并不需要重新生成id，直接加1即可
                subId += 1;
            }

            //new Img()不能放置循环外，否则只会不停更新此img实例，不会增加
            Img img = new Img();
            img.setSourceName(fileInfo.getName());
            img.setSubDir(subdirectory);
//            if(owner!=null){
            img.setOwner(owner);
//            }
            img.setTag(tag);
            img.setSubId(subId);
            img.setEnabled(true);
            imgRepository.save(img);
//            System.out.println("文件名：" + img.getSourceName() + "\n" + "子文件夹：" + img.getSubDir()); //测试用
        }
    }

    /**
     * 如果出现相同的图片，可能是因为两张图片本身就相同或相似
     * 或者数据库有两个sourcename（文件名）有相同的值，请删除
     * <p>
     * 第一种方法，生成随机数，通过subId依次查询
     * 优点，满足要求，且避免了查询为null的空数据
     * 缺点：在subId不连续的情况下会进行多次查询，直到满足数量。单个查询，增加了数据库的压力
     * 适合一定要满足数量要求时使用
     */

    public List<Img> findRandomAndEnabledImgsByTagLimitNum(String tag, int num) {
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
                img = imgRepository.findByTagAndSubIdAndEnabled(tag, randomNum, true); //查询此img
                if (img != null) {
                    imgs.add(img);
                }
                randomNumList.add(randomNum); //将此随机数添加至列表，避免重复查询
            }
        }
        return imgs;
    }

    /**
     * 第二种方法，生成随机数，通过subId查询
     * 优点：一次性查询完毕，数据库压力小
     * 缺点：在subId不连续的情况下，可能会查询到空数据，虽然最终结果里空数据被省去，不会有null值，但是数据的数量要求不满足
     * 适合subId连续时使用
     */

    public List<Img> findRandomAndEnabledImgsByTagLimitNum2(String tag, int num) {
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
        return imgRepository.findByTagAndSubIdAndEnabledIn(tag, randomNumSet, true);
    }

    /**
     * 第三种方法，从数据库，取出全部符合tag标签的数据，从中随机抽取数据
     * 优点：一次性查询完毕，数据库压力小
     * 缺点：数据量太大时，list在内存中存储的数据太大
     * 适合数据量不是异常大时使用
     */

    public List<Img> findRandomAndEnabledImgsByTagLimitNum3(String tag, int num) {
        List<Img> imgs = imgRepository.findByTagAndEnabled(tag, true);

        List<Long> subIds = new ArrayList<>();
        List<Img> results = new ArrayList<>();
        Set<Integer> randomNumSet = new HashSet<>(); //定义Set，避免随机数的重复
        long count = imgRepository.countByTag(tag);
        int randomNum;

        for (Img img : imgs) {
            subIds.add(img.getSubId());
        }

        if (count < num) {
            num = (int) count;
        }

        //判定sudIds的大小不等于0的情况，防止数据库没有一张符合要求的图片
        while (randomNumSet.size() < num && subIds.size() != 0) {
            //生成随机数，范围subId的索引范围
            //因为生成的随机数是左闭右开，需要+1，而subId.size需要-1，正好抵消
            randomNum = ThreadLocalRandom.current().nextInt(0, subIds.size());
            randomNumSet.add(randomNum);
        }
        for (int id : randomNumSet) {
            results.add(imgs.get(id));
        }

        return results;
    }

}
