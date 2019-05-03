package com.riri.emojirecognition.service.impl;

import com.riri.emojirecognition.component.deeplearning.ClassifierWithDeepLearning4j;
import com.riri.emojirecognition.domain.Model;
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
        init(modelService.findByEnabled(true), 0);
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
            logger.warn("找不到模型，回退到默认模型");
            try {
                classifierModel.setPath(new ClassPathResource(modelDefaultPath).getFile().getPath());
            } catch (IOException e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("模型初始化成功");
    }

    public void init(Long modelId, int flag) { //通过模型id初始化模型
        init(modelService.findById(modelId), flag);
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

    public void enableModelById(Long modelId, int flag) {
        if (flag == 0) {
            Model oldModel = modelService.findByEnabled(true);
            Model newModel;

            if (oldModel != null) {
                if (!oldModel.getId().equals(modelId)) {//当新旧id相同时。则无需再修改数据库和初始化模型，否则在后续操作还会改回来
                    oldModel.setEnabled(null); //不要设置为false，因为设置了唯一限制
                    modelService.save(oldModel);

                    newModel = modelService.findById(modelId);
                    newModel.setEnabled(true); //标记启用此模型
                    modelService.save(newModel);

                    init(modelId, flag); //初始化此模型
                }
            } else { //数据库无启用的模型时，则仅需对新启用的模型操作
                newModel = modelService.findById(modelId);
                newModel.setEnabled(true); //标记启用此模型
                modelService.save(newModel);

                init(modelId, flag);
            }

        } else {//flag不为0时为临时使用模型，不需要对数据库操作
            if(classifier.getSelectedModelId()!=null) {
                if (!classifier.getSelectedModelId().equals(modelId)) { //当新旧id相同时，则不进行初始化操作，防止重复加载模型
                    init(modelId, flag);
                }
            }
            else {
                init(modelId,flag);
            }
        }
    }
}
