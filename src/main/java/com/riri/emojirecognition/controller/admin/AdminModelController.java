package com.riri.emojirecognition.controller.admin;

import com.riri.emojirecognition.component.deeplearning.ClassifierWithDeepLearning4j;
import com.riri.emojirecognition.domain.Model;
import com.riri.emojirecognition.service.ClassifyService;
import com.riri.emojirecognition.service.ModelService;
import com.riri.emojirecognition.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/model")
public class AdminModelController {

    @Value("${path.model.base-path}")
    private String modelBasePath;

    private final ModelService modelService;

    private final ClassifyService classifyService;

    @Autowired
    public AdminModelController(ModelService modelService, ClassifyService classifyService) {
        this.modelService = modelService;
        this.classifyService = classifyService;
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
    public void upload(@RequestParam("file") MultipartFile mFile) {
        if (!mFile.isEmpty()) {

            //原始文件名
            String originalFilename = mFile.getOriginalFilename();

            //给图片新的uuid名
            String newFilename = FileUtil.getUUIDFilename(mFile.getOriginalFilename());

            String path = modelBasePath + newFilename;

            //调用上传工具类
            FileUtil.upload(mFile, path);

            Model model = new Model();
            model.setName(originalFilename);
            model.setPath(path);
            modelService.save(model);
        }
    }

    @RequestMapping("enable")
    public void enable(@RequestParam("id") Long id, @RequestParam(value = "flag",required = false,defaultValue = "0") int flag) {
        classifyService.enable(id,flag);
    }

}
