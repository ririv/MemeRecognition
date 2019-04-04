package com.riri.emojirecognition.controller;

import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.service.ImgService;
import com.riri.emojirecognition.util.FileUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;


@RestController
@RequestMapping("/api/v1/img")
public class ImgController {

    private final ImgService imgService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public ImgController(ImgService imgService,ResourceLoader resourceLoader) {
        this.imgService = imgService;
        this.resourceLoader = resourceLoader;
    }

    @Value("${web.upload-path}")
    private String uploadPath; // 要上传的目标文件存放路径

    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public Img findImg(@PathVariable Long id) {
        return imgService.findById(id);
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public List<Img> findImg(@RequestParam("tag") String tag) {
        return imgService.findRandomImgsByTagLimitNum3(tag,5);
    }

    //显示单张图片
    @RequestMapping("show")
    public ResponseEntity showImg(String filename) {

        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + uploadPath + filename));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("upload")
    public Map<String,Object> upload(@RequestParam("file") MultipartFile mfile){
        // 上传成功或者失败的提示
        String msg;
        Optional<String> classifyResult;
        String tag = null;
        List<Img> relatedImgs = null;
        Map<String,Object> resultMap = new HashMap<>();


        if (!mfile.isEmpty()) {
            //调用上传工具类
            //图片新名
            String uuidFilename = FileUtil.getUUIDFilename(mfile.getOriginalFilename());
            File file = FileUtil.upload(mfile, uploadPath, uuidFilename);

            classifyResult= imgService.classify(file);

            // 识别成功，给出页面提示
            if (classifyResult.isPresent()) {
                msg = "识别成功";
                tag = classifyResult.get();
                relatedImgs = imgService.findRandomImgsByTagLimitNum3(tag,10);
            }
            else {msg = "无法识别";}

            //保存到repo
            Img img = new Img();
            //保存新的UUID名
            img.setName(uuidFilename);
            img.setSubDir(uploadPath);
//            img.setSourcename(mfile.getOriginalFilename().substring(mfile.getOriginalFilename().lastIndexOf("."));
            String originalFilename = mfile.getOriginalFilename();
            if (originalFilename != null) {
                img.setSourcename(FileUtil.getSourceName(originalFilename));
            }
            imgService.save(img);
        } else {
            msg = "图片上传失败！";
        }

        //构建json
        resultMap.put("msg",msg);
        resultMap.put("tag",tag);
        resultMap.put("relatedImgs",relatedImgs);

        return resultMap;
    }
}
