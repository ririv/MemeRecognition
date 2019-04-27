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

    @PostConstruct
    private void init() {
        //在程序启动时从数据库中寻找启用的模型，并载入此模型的相关配置
        //此时使用的是启用的模型，的flag为0
        init(modelService.findByEnabled(true), 0);
    }

    public void init(Model model, int flag) {
        ClassifierWithDeepLearning4j.Model classifierModel = new ClassifierWithDeepLearning4j().new Model();
        if (model != null) {

            classifierModel.setId(model.getId());
            classifierModel.setPath(model.getPath());
            classifierModel.setHeight(model.getHeight());
            classifierModel.setWidth(model.getWidth());
            classifierModel.setChannels(model.getChannels());

            String[] labels = model.getLabels().split(",");
            classifierModel.setLabels(labels);
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

    public void init(Long id, int flag) { //通过模型id初始化模型
        init(modelService.findById(id), flag);
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

    public void enable(Long id, int flag) {
        Model oldModel = modelService.findByEnabled(true);
        if (oldModel != null) {
            oldModel.setEnabled(null); //不要设置为false，因为设置了唯一限制
            modelService.save(oldModel);
        }

        Model model = modelService.findById(id);
        if (flag == 0) { //flag为1时临时使用所选择的模型，不必启用
            model.setEnabled(true); //标记启用此模型
        }
        modelService.save(model);

        init(id, flag); //初始化此模型
    }
}
