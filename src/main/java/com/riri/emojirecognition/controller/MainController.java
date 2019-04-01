package com.riri.emojirecognition.controller;

import com.riri.emojirecognition.util.FileUtil;
import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class MainController {

    private final ResourceLoader resourceLoader;
    private final ImgService imgService;

    @Autowired
    //构造函数，确保名称一致
    public MainController(ResourceLoader resourceLoader, ImgService imgService) {
        this.resourceLoader = resourceLoader;
        this.imgService = imgService;
    }

    @GetMapping("/main")
    public ModelAndView main() {
        return new ModelAndView("main.html");
    }

//    @PostMapping("/emoji-upload")
//    public String fileUpload(@RequestParam(value = "file") MultipartFile file, Model model, HttpServletRequest request) {
//        if (file.isEmpty()) {
//            System.out.println("文件为空");
//        }
//        String filename = file.getOriginalFilename();  // 文件名
//        String suffixName = filename.substring(filename.lastIndexOf("."));  // 后缀名
//        String filePath = "D://temp-rainy//"; // 上传后的路径
//        filename = UUID.randomUUID() + suffixName; // 新文件名
//        File dest = new File(filePath + filename);
//        if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
//            dest.getParentFile().mkdirs(); //不存在则建立目录
//        }
//        try {
//            file.transferTo(dest); //保存文件
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String filename = "/temp-rainy/" + filename;
//        model.addAttribute("filename", filename);
//        return "file";
//    }







}
