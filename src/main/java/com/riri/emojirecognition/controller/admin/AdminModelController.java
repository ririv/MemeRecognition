package com.riri.emojirecognition.controller.admin;

import com.riri.emojirecognition.domain.Model;
import com.riri.emojirecognition.service.ModelService;
import com.riri.emojirecognition.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/model")
public class AdminModelController {
    private final ModelService modelService;

    @Autowired
    public AdminModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping(value = "operate/{id}")
    public Model get(@PathVariable Long id) {
        return modelService.findById(id);
    }

    @PutMapping(value = "operate/{id}")
    public Model update(@PathVariable Long id, @RequestBody Model user) {
        return modelService.updateById(id, user);
    }

    @DeleteMapping(value = "operate/{id}")
    public void delete(@PathVariable Long id) {
        modelService.deleteById(id);
    }

    @PostMapping(value = "create")
    public Model create(@RequestBody Model user) {
        return modelService.createModel(user);
    }

    @GetMapping(value = "query")
    public Page findAll(Pageable pageable) {
        return modelService.findAll(pageable);
    }

    @PostMapping("upload")
    public void upload(@RequestParam("file") MultipartFile mfile) {
        if (!mfile.isEmpty()) {
            //原始文件名
            String originalFilename = mfile.getOriginalFilename();

            //给图片新的uuid名
            String newFilename = FileUtil.getUUIDFilename(mfile.getOriginalFilename());

//            String path = imgBasePath + imgModelPath + newFilename;

            //调用上传工具类
//            File file = FileUtil.upload(mfile, path);

            Model model = new Model();
            model.setName(originalFilename);
//            model.setPath(path);

        }
    }

    @GetMapping("123")
    public void abc(){
    }
}
