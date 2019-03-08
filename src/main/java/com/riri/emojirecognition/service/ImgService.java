package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.repository.ImgRepository;
import com.riri.emojirecognition.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImgService {

    @Value("${web.default-path}")
    public String defaultPath;

    @Autowired
    private ImgRepository imgRepository;

    public Img save(Img img) {
        return imgRepository.save(img);
    }

    public List<Img> saveAll(List<Img> imgs){
        return imgRepository.saveAll(imgs);
    }


    public Img findById(Long id) {
        return imgRepository.findById(id).orElse(null);
    }

    public void batchInsertToDbByFolder(String dirpath, String owner){
        String parentdirectory = defaultPath.replace("\\","/");
        String subdirectory;
        String tab;

        List<FileUtil.FileInfo> imgList = FileUtil.traverseFolder(dirpath);

            for(FileUtil.FileInfo fi: imgList) {
                //将所有\改为/，使格式统一化，方便删除父路径
                subdirectory = fi.getPath().replace("\\","/");
                //去掉父路径
                subdirectory = subdirectory.replaceAll(parentdirectory,"");

//                //通过最后一个文件夹名存储tab,再去掉"/",此方法逻辑不好
//                tab  = subdirectory
//                        //先去掉最后一个字符"/"
//                        .substring(0,subdirectory.length() - 1)
//                        //再通过获得倒数第二个"/"位置+1来获得最后一个子文件夹名
//                        .substring(subdirectory.lastIndexOf("/")+1);

                //通过"/"切割字符串获得最后一个元素即为最后的子文件夹名做为tab
                String[] subdirArray = subdirectory.split("/");
//                for (String s: subdirArray){System.out.println(s);}//测试用
                tab = subdirArray[subdirArray.length-1];

                //new Img()不能放置循环外，否则只会不停更新一个img，不会增加
                Img img = new Img();
                img.setName(fi.getName());
                img.setSubdirectory(subdirectory);
//                if(owner!=null){
                    img.setOwner(owner);
//                }
                img.setTab(tab);
                imgRepository.save(img);
                System.out.println("图片名："+img.getName()+"\n"+"子文件夹："+img.getSubdirectory()); //测试用
            }
    }
}
