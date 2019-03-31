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

@RestController
@RequestMapping("/")
public class IndexController {

    private final ResourceLoader resourceLoader;
    private final ImgService imgService;

    @Autowired
    //构造函数，确保名称一致
    public IndexController(ResourceLoader resourceLoader, ImgService imgService) {
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

    @Value("${web.upload-path}")
    private String uploadPath; // 要上传的目标文件存放路径

    @GetMapping("/img-upload")
    public ModelAndView imgupload() {
        return new ModelAndView("img-upload");
    }

    @RequestMapping("fileUpload")
    public ModelAndView upload(@RequestParam("filename") MultipartFile mfile, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response){

        // 上传成功或者失败的提示
        String msg;

        if (mfile.isEmpty()) {
            return new ModelAndView("img-upload");
        }

        //调用上传工具类
        //图片新名
        String uuidFilename = FileUtil.getUUIDFilename(mfile.getOriginalFilename());
        File file = FileUtil.upload(mfile, uploadPath, uuidFilename);

        if (file.length() != 0) {
            String result = imgService.classify(file);
//            System.out.println(result);

            // 上传成功，给出页面提示
            msg = "无法识别";
            if (result != null) {
                msg = result;
            }
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

        // 显示图片
        map.put("msg", msg);
        //图片原始名
        //map.put("filename", mfile.getOriginalFilename());
        //图片新名，使用新名必须修改，否则无法映射文件路径
        map.put("filename", uuidFilename);

//            RequestDispatcher requestDispatcher =request.getRequestDispatcher("index");
//            //调用forward()方法，转发请求     
//            requestDispatcher.forward(request,response);
        return new ModelAndView("img-upload");
    }


     //显示单张图片
    @RequestMapping("show")
    public ResponseEntity showImg(String filename) {

        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + uploadPath + filename));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
