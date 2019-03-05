package com.riri.emojirecognition.web.controller;

import com.riri.emojirecognition.utils.FileUtil;
import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/")
public class IndexController {

    private static final String INDEX = "index";

    private final ResourceLoader resourceLoader;

    @Autowired
    //构造函数，确保名称一致
    public IndexController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    private ImgService imgService;

    @RequestMapping("/admin/show")
    public String getIndex() {
        return INDEX;
    }

    @GetMapping("/main")
    public ModelAndView main() {
        return new ModelAndView("main");
    }

//    @PostMapping("/emoji-upload")
//    public String fileUpload(@RequestParam(value = "file") MultipartFile file, Model model, HttpServletRequest request) {
//        if (file.isEmpty()) {
//            System.out.println("文件为空");
//        }
//        String fileName = file.getOriginalFilename();  // 文件名
//        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
//        String filePath = "D://temp-rainy//"; // 上传后的路径
//        fileName = UUID.randomUUID() + suffixName; // 新文件名
//        File dest = new File(filePath + fileName);
//        if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
//            dest.getParentFile().mkdirs(); //不存在则建立目录
//        }
//        try {
//            file.transferTo(dest); //保存文件
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String filename = "/temp-rainy/" + fileName;
//        model.addAttribute("filename", filename);
//        return "file";
//    }

    @Value("${web.default-path}")
    private String defaultPath;

    @RequestMapping("fileUpload")
    public ModelAndView upload(@RequestParam("fileName") MultipartFile file, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 要上传的目标文件存放路径
        String defaultPath = this.defaultPath;
        // 上传成功或者失败的提示
        String msg;

        if (file.isEmpty()) {
            return new ModelAndView("main");
        }
        //调用上传工具类
//        if (FileUtil.upload(file, defaultPath, file.getOriginalFilename())) {//图片原始名
        //图片新名
        String newFileName = FileUtil.getUUIDFileName(file.getOriginalFilename());
        if (FileUtil.upload(file, defaultPath, newFileName)) {
            // 上传成功，给出页面提示
            msg = "上传成功！";

            //保存到repo
            Img img = new Img();
            //保存新的UUID名
            img.setName(newFileName);
            img.setSubdir(defaultPath);
//            img.setSourcename(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String originalFileName = file.getOriginalFilename();
            if (originalFileName != null) {
                img.setSourcename(FileUtil.getSourceName(originalFileName));
            }
            imgService.save(img);
        } else {
            msg = "上传失败！";

        }

        // 显示图片
        map.put("msg", msg);
        //图片原始名
        //map.put("fileName", file.getOriginalFilename());
        //图片新名，使用新名必须修改，否则无法映射文件路径
        map.put("fileName", newFileName);

//            RequestDispatcher requestDispatcher =request.getRequestDispatcher("index");
//            //调用forward()方法，转发请求     
//            requestDispatcher.forward(request,response);
        return new ModelAndView("main");

    }


    /**
     * 显示单张图片
     *
     * @return
     */
    @RequestMapping("show")
    public ResponseEntity showImg(String fileName) {

        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + defaultPath + fileName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}
