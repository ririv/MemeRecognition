package com.riri.emojirecognition.controller.admin;

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

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/admin/model")
public class AdminModelAndClassifierController {

    @Value("${path.base.model}")
    private String modelBasePath;

    private final ModelService modelService;

    private final ClassifyService classifyService;

    @Autowired
    public AdminModelAndClassifierController(ModelService modelService, ClassifyService classifyService) {
        this.modelService = modelService;
        this.classifyService = classifyService;
    }

    @GetMapping(value = "current-classifier")
    public Map current() {
        return classifyService.getCurrentModelsInfoOfClassifier();
    }

    @GetMapping(value = "operate/{id}")
    public Model get(@PathVariable Long id) {
        return modelService.findById(id);
    }

    @PutMapping(value = "operate/{id}")
    public Model update(@PathVariable Long id, @RequestBody Model transferModel) {
        Model model = modelService.updateById(id,transferModel);

        if (model.isEnabled() != null && model.isEnabled()) {
            classifyService.enableModel(model, 0);
        }

        return model;
    }

    @DeleteMapping(value = "operate/{id}")
    public void delete(@PathVariable Long id) {
        modelService.deleteById(id);
    }

    @PostMapping(value = "operate")
    public Model add(@RequestBody Model transferModel) {

        Model model = modelService.addModel(transferModel); //先保存数据库，再启用
        if (model.isEnabled() != null && model.isEnabled()){
            //一定不可以直接对用户提交的model操作，而要到数据库中返回的Model操作
            // 否则因为id异常问题可能引发错误
            classifyService.enableModel(model, 0);
        }
        return model;
    }

    @GetMapping(value = "query")
    public Page findAll(Pageable pageable) {
        return modelService.findAll(pageable);
    }

    @PostMapping("create")
    public Model createWithUpload(@RequestPart("file") MultipartFile mFile, @RequestPart("model") Model transferModel) {
        Model model = null;
        if (!mFile.isEmpty()) {

            //原始文件名
            String originalFilename = mFile.getOriginalFilename();
            //给图片新的uuid名
            String newFilename = FileUtil.getUUIDFilename(mFile.getOriginalFilename());
            String path = modelBasePath + newFilename;

            //调用上传工具类
            FileUtil.upload(mFile, path);

            Model newModel = new Model();
            newModel.setName(originalFilename);
            newModel.setPath(path);
            newModel.setWidth(transferModel.getWidth());
            newModel.setHeight(transferModel.getHeight());
            newModel.setChannels(transferModel.getChannels());
            newModel.setLabels(transferModel.getLabels());
            newModel.setDescription(transferModel.getDescription());
            newModel.setUpdateTime(new Timestamp(new Date().getTime())); //获得当前时间
            newModel.setEnabled(transferModel.isEnabled());
            model = modelService.addModel(newModel);

            if (model.isEnabled() != null && model.isEnabled()) { //如果是启用的，则立马启用它
                classifyService.enableModel(model, 0);
            }

        }
        return model;
    }

    @RequestMapping("enable")
    public void enable(@RequestParam("id") Long id, @RequestParam(value = "flag", required = false, defaultValue = "0") int flag) {
        classifyService.enableModelById(id, flag);
    }

}
