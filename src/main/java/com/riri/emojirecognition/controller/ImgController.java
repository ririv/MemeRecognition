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
    public ImgController(ImgService imgService, ResourceLoader resourceLoader) {
        this.imgService = imgService;
        this.resourceLoader = resourceLoader;
    }

    @Value("${path.img.user-path}")
    private String imgUserPath;

    @Value("${path.img.base-path}")
    private String imgBasePath;

    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public Img findImg(@PathVariable Long id) {
        return imgService.findById(id);
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public List<Img> findImg(@RequestParam("tag") String tag) {
        return imgService.findRandomImgsByTagLimitNum3(tag, 5);
    }

    //显示单张图片
    @RequestMapping("show")
    public ResponseEntity showImg(String filename) {

        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + imgUserPath + filename));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile mfile) {
        // 上传成功或者失败的提示
        String msg;
        boolean isSuceess;
        Optional<String> classifyResult;
        String tag = null;
        List<Img> relatedImgs = null;
        Map<String, Object> resultMap = new LinkedHashMap<>();


        if (!mfile.isEmpty()) {
            //原始文件名
            String originalFilename = mfile.getOriginalFilename();

            //给图片新的uuid名
            String newFilename = FileUtil.getUUIDFilename(mfile.getOriginalFilename());

            //调用上传工具类
            File file = FileUtil.upload(mfile,  imgBasePath + imgUserPath +newFilename);

            classifyResult = imgService.classify(file);

            // 识别成功，给出页面提示
            if (classifyResult.isPresent()) {
                msg = "识别成功";
                tag = classifyResult.get();
                relatedImgs = imgService.findRandomImgsByTagLimitNum3(tag, 10);
                isSuceess=true;
            } else {
                msg = "无法识别";
                isSuceess=false;
            }

            //保存到repo
            Img img = new Img();
            //保存新的UUID名
            img.setSourcename(newFilename);
            img.setSubDir(imgUserPath);
            img.setTag(tag);

            if (originalFilename != null) {
                img.setName(FileUtil.getFilenameWithoutExt(originalFilename));
            }

            if (tag != null) {
                Long subId = imgService.findMaxSubIdByTag(tag);
                if(subId==null){
                    subId = 0L;
                }
                img.setSubId(subId+1);
            }

            imgService.save(img);
        } else {
            msg = "图片上传失败！";
            isSuceess=false;
        }

        //构建json
        resultMap.put("isSuccess",isSuceess);
        resultMap.put("msg", msg);
        resultMap.put("tag", tag);
        resultMap.put("relatedImgs", relatedImgs);

        return resultMap;
    }
}
