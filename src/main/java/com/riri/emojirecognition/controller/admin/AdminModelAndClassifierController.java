package com.riri.emojirecognition.controller.admin;

import com.riri.emojirecognition.domain.Model;
import com.riri.emojirecognition.service.ClassifyService;
import com.riri.emojirecognition.service.ModelService;
import com.riri.emojirecognition.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    private String uploadModelName;

    private String uploadModelPath;

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
    public Model update(@PathVariable Long id, @RequestBody Model model) {
        if (model.isEnabled()) {
            classifyService.enableModel(model, 0);
        }

        return modelService.updateById(id, model);
    }

    @DeleteMapping(value = "operate/{id}")
    public void delete(@PathVariable Long id) {
        modelService.deleteById(id);
    }

    @PostMapping(value = "operate")
    public Model add(@RequestBody Model model) {
        return modelService.addModel(model);
    }

    @GetMapping(value = "query")
    public Page findAll(@PageableDefault Pageable pageable) {
        return modelService.findAll(pageable);
    }

    /**
     * 我们可以采用线程等待与唤醒的方式完成文件的上传，与文件信息插入至数据库
     * 待上传完成后，upload方法最后会notify
     *
     * @param mFile 上传的model文件
     */
    @PostMapping("create/upload")
    public void upload(@RequestParam("file") MultipartFile mFile) {
        if (!mFile.isEmpty()) {

            //给图片新的uuid名
            String newFilename = FileUtil.getUUIDFilename(mFile.getOriginalFilename());
            String path = modelBasePath + newFilename;

            uploadModelPath = path;
            uploadModelName = mFile.getOriginalFilename();

            //调用上传工具类
            FileUtil.upload(mFile, path);
        }

        synchronized (this) {
            uploadModelName = mFile.getOriginalFilename();
            this.notify();
        }
    }

    /**
     * 此方法是与upload方法配合使用的，线程进入wait，等待上传完成被notify
     * 再执行插入数据库和启用模型的操作
     *
     * @param transferModel model json数据
     * @return repo返回的model对象
     */
    @PostMapping(value = "create/add")
    public Model addForUpload(@RequestBody Model transferModel) {
        Model model;
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (transferModel.getName() == null) {
                transferModel.setName(uploadModelName);
                System.out.println(transferModel.getName());
            }
            System.out.println("上传文件名：" + uploadModelName);
            transferModel.setPath(uploadModelPath);
            System.out.println("上传路径：" + uploadModelPath);

            transferModel.setUpdateTime(new Timestamp(new Date().getTime()));

            //如果模型是启用的，必须先将数据库中已启用的model设为非启用
            //否则数据库将存在两个启用的模型
            if(transferModel.isEnabled()){
                Model oldModel = modelService.findByEnabled(true);
                oldModel.setEnabled(false);
                modelService.save(oldModel);
            }

            //先保存数据库，再启用，否则enableModel方法将将会保存模，因为id为空的原因，将会重复保存一次
            model = modelService.addModel(transferModel);

            if (model.isEnabled()) {
                //一定不可以直接对用户提交的model操作，而要到数据库中返回的Model操作
                // 否则因为id异常问题可能引发错误
                classifyService.enableModel(model, 0);
            }
            uploadModelPath = null;
        }

        return model;
    }

    /**
     * 这里采用可以同时上传文件及其json数据的方式
     * 上传采用form-data，但需要对两个参数设置不同的content-type
     * file为multipart/form-data，model为application/json
     * 因为设置不同的参数的content-type是一件很麻烦的是，所以此API不建议使用
     *
     * @param mFile         model文件
     * @param transferModel model的json数据
     * @return repo返回的model对象
     */
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

            if (model.isEnabled()) { //如果是启用的，则立马启用它
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
