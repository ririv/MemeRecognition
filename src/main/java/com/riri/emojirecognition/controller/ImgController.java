package com.riri.emojirecognition.controller;

import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.service.ClassifyService;
import com.riri.emojirecognition.service.ImgService;
import com.riri.emojirecognition.util.FileUtil;

import javafx.util.Pair;
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
    private final ClassifyService classifyService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public ImgController(ImgService imgService, ClassifyService classifyService, ResourceLoader resourceLoader) {
        this.imgService = imgService;
        this.classifyService = classifyService;
        this.resourceLoader = resourceLoader;
    }

    @Value("${path.sub.img.user}")
    private String userImgSubPath;

    @Value("${path.base.img}")
    private String imgBasePath;

    @RequestMapping(value = "details/{id}", method = RequestMethod.GET)
    public Img findImg(@PathVariable Long id) {
        return imgService.findById(id);
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public List<Img> findImg(@RequestParam("tag") String tag, @RequestParam(required = false, defaultValue = "20") int num) {
        return imgService.findRandomAndEnabledImgsByTagLimitNum3(tag, num);
    }

    //映射本地图片到网址
    //也可以在WebMvcConfig中配置
    @RequestMapping("show")
    public ResponseEntity showImg(String filename) {

        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + userImgSubPath + filename));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("upload")
    public Map<String, Object> classify(@RequestParam("file") MultipartFile mFile,
                                        @RequestParam(required = false, defaultValue = "20") int num,
                                        @RequestParam(value = "flag", required = false, defaultValue = "0") int flag,
                                        @RequestParam(value = "model-id", required = false) Long modelId) {
        // 上传成功或者失败的提示
        String msg;
        boolean isSuccess;
        Optional<Pair<String, Float>> classifyResult;
        String tag = null;
        Float proba = null;
        List<Img> relatedImgs = null;
        Map<String, Object> resultMap = new LinkedHashMap<>();


        if (!mFile.isEmpty()) {
            //原始文件名
            String originalFilename = mFile.getOriginalFilename();

            //给图片新的uuid名
            String newFilename = FileUtil.getUUIDFilename(mFile.getOriginalFilename());

            //调用上传工具类
            File file = FileUtil.upload(mFile, imgBasePath + userImgSubPath + newFilename);

            if (flag != 0) {
                classifyService.enable(modelId, flag);
            }

            classifyResult = classifyService.classify(file, flag);

            // 识别成功，给出页面提示
            if (classifyResult.isPresent()) {
                msg = "识别成功";
                tag = classifyResult.get().getKey();
                proba = classifyResult.get().getValue();

                relatedImgs = imgService.findRandomAndEnabledImgsByTagLimitNum3(tag, num);
                isSuccess = true;
            } else {
                msg = "无法识别";
                isSuccess = false;
            }

            //保存到repo
            Img img = new Img();
            //保存新的UUID名
            img.setSourceName(newFilename);
            img.setSubDir(userImgSubPath);
            img.setTag(tag);

            if (originalFilename != null) {
                img.setName(FileUtil.getFilenameWithoutExt(originalFilename));
            }

            if (tag != null) {
                Long subId = imgService.findMaxSubIdByTag(tag);
                if (subId == null) {
                    subId = 0L;
                }
                img.setSubId(subId + 1);
            }
            img.setEnabled(false);

            imgService.save(img);
        } else {
            msg = "图片上传失败！";
            isSuccess = false;
        }

        //构建json
        resultMap.put("isSuccess", isSuccess);
        resultMap.put("msg", msg);
        resultMap.put("tag", tag);
        resultMap.put("proba", proba);
        resultMap.put("relatedImgs", relatedImgs);

        return resultMap;
    }
}
