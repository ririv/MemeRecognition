package com.riri.emojirecognition.service.impl;

import com.riri.emojirecognition.component.deeplearning.ClassifierWithDeepLearning4j;
import com.riri.emojirecognition.domain.Model;
import com.riri.emojirecognition.exception.ModelImportException;
import com.riri.emojirecognition.service.ClassifyService;
import com.riri.emojirecognition.service.ModelService;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ClassifyServiceImpl implements ClassifyService {

    private static final Logger logger = LoggerFactory.getLogger(ClassifyServiceImpl.class);

    private ClassifierWithDeepLearning4j classifier;

    private final ModelService modelService;

    @Value("${classify.proba}")
    private float classifyProba;

    @Value("${model.default.resourcePath}")
    private String modelDefaultPath;
    @Value("${model.default.height}")
    private int modelDefaultHeight;
    @Value("${model.default.width}")
    private int modelDefaultWidth;
    @Value("${model.default.channels}")
    private int modelDefaultChannels; //通道
    @Value("${model.default.labels}")
    private String[] modelDefaultLabels;

    @Autowired
    public ClassifyServiceImpl(ClassifierWithDeepLearning4j classifier, ModelService modelService) {
        this.classifier = classifier;
        this.modelService = modelService;
    }

    /**
     * 使用@PostConstruct在程序启动时从数据库中寻找启用的模型，并载入此模型的相关配置
     * 此时使用的是启用的模型，flag为0
     */
    @PostConstruct
    private void init() {
        try {
            init(modelService.findByEnabled(true), 0);
        } catch (ModelImportException e) {
            throw new ModelImportException("初始化数据库中启用的模型失败，清检查该模型");
        }
    }

    public void init(Model model, int flag) {
        ClassifierWithDeepLearning4j.Model classifierModel = classifier.new Model();

        if (model != null) {
            classifierModel.setId(model.getId());
            classifierModel.setPath(model.getPath());
            classifierModel.setHeight(model.getHeight());
            classifierModel.setWidth(model.getWidth());
            classifierModel.setChannels(model.getChannels());
            classifierModel.setLabels(model.getLabels());
        } else { //采用存放在resource/model下默认的模型
            logger.warn("数据库中找不到已启用的模型，回退到默认模型");
            try {
                classifierModel.setPath(new ClassPathResource(modelDefaultPath).getFile().getPath());
            } catch (IOException e) {
                logger.warn("默认模型回退失败，无效的资源路径");
                e.printStackTrace();
            }

            classifierModel.setHeight(modelDefaultHeight);
            classifierModel.setWidth(modelDefaultWidth);
            classifierModel.setChannels(modelDefaultChannels);
            classifierModel.setLabels(modelDefaultLabels);

        }

        //调用classifier.init()来初始化模型
        try {
            classifier.init(classifierModel, flag);
            logger.info("模型初始化成功");
        } catch (Exception e) {
            logger.warn("模型初始化失败");
            if (model != null)
                throw new ModelImportException(model.getId());
            else {
                throw new ModelImportException();
            }
        }
    }

    public void init(Long modelId, int flag) { //通过模型id初始化模型
        try {
            init(modelService.findById(modelId), flag);
        } catch (ModelImportException e) {
            throw new ModelImportException(modelId);
        }
    }

    public Optional<Pair<String, Float>> classify(File image, int flag) {
        Pair<String, Float> labelWithProba = null;
        String label;
        Float proba;

        try {
            labelWithProba = classifier.classify(image, flag);
            label = labelWithProba.getKey();
            proba = labelWithProba.getValue();
            logger.info("The predict label:" + label);
            logger.info("The predict proba:" + proba.toString());
            if (proba < classifyProba) {
                labelWithProba = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(labelWithProba);
    }

    public void enableModelById(Long targetModelId, int flag) {
        Model targetModel = modelService.findById(targetModelId);
        enableModel(targetModel, flag);
    }

    /**
     * @param targetModel 需要启用的目标模型
     * @param flag        为1时为对被应用的主要模型enabledModel操作，记录至数据库
     *                    为2时对被选择的备用模型selectedModel操作，临时使用，不记录至数据库
     */
    public void enableModel(Model targetModel, int flag) {
        if (flag == 0) {
            Model oldModel = modelService.findByEnabled(true); //找到数据库中原先被启用的旧模型

            if (oldModel != null) { //数据库中有启用的模型时
                //当旧模型id与目标模型id相同时。则不操作，因为是同一个模型，且此模型是已启用的模型
                //这里为两id不同的情况，需要保存并初始化
                if (!oldModel.getId().equals(targetModel.getId())) {
                    oldModel.setEnabled(false); //旧模型设为非启用，不要设置为false，因为设置了唯一限制
                    modelService.save(oldModel); //保存目标模型为启用到数据库并初始化它
                    init(targetModel, flag);
                }
            } else { //无启用模型时
                init(targetModel, flag);
            }

            //如果目标模型是非启用状态，则修改此值保存至数据库
            //也有可能目标模型是启用的状态，比如创建新模型立马启用它或者是启用数据库中已启用的模型，此时就无需再保存一次到数据库
//            if(!targetModel.isEnabled()) {
            if (targetModel.isEnabled() && oldModel != null && !oldModel.getId().equals(targetModel.getId())) { //模型已启用状态且与数据库中已启用的模型不同
                targetModel.setEnabled(true); //标记启用此模型
                modelService.save(targetModel); //保存目标模型为启用到数据库并初始化它
            }


        } else {//flag不为0时为临时使用模型SelectedModel，不需要对数据库操作
            if (classifier.getSelectedModelId() != null) { //SelectedModel里有启用模型时
                if (!classifier.getSelectedModelId().equals(targetModel.getId())) { //与上文中对应的if语句意义相同
                    init(targetModel, flag);
                }
            } else {
                init(targetModel, flag);
            }
        }
    }

    public Map<String, Object> getCurrentModelsInfoOfClassifier() {
        Map<String, Object> infoMap = new LinkedHashMap<>();

        infoMap.put("enabledModelId", classifier.getEnabledModelId());
        infoMap.put("enabledModelLabels", classifier.getEnabledModelLabels());

        infoMap.put("selectedModelId", classifier.getSelectedModelId());
        infoMap.put("SelectedModelLabels", classifier.getSelectedModelLabels());

        return infoMap;
    }

}
