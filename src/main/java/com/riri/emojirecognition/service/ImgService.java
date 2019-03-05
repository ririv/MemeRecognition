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

    public void batchInsertToDbByFolder(String dirpath, String owner){
        String parentdirectory = defaultPath.replace("\\","/");
        String subdirectory;

        List<FileUtil.FileInfo> imgList = FileUtil.traverseFolder(dirpath);

            for(FileUtil.FileInfo fi: imgList) {
                subdirectory = fi.getPath().replace("\\","/");
                subdirectory = subdirectory.replaceAll(parentdirectory,"");
                //不能放置循环外，否则只会不停更新一行，不会增加行
                Img img = new Img();
                img.setName(fi.getName());
                img.setSubdir(subdirectory);
//                if(owner!=null){
                    img.setOwner(owner);
//                }
                imgRepository.save(img);
                System.out.println(img.getName()+"\n"+img.getSubdir()); //测试用
            }
    }
}
