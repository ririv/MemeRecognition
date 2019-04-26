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
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
public class ClassifyServiceImpl implements ClassifyService {

    private static final Logger logger = LoggerFactory.getLogger(ClassifyServiceImpl.class);

    private ClassifierWithDeepLearning4j classifier;

//    private ModelService modelService;
//
    @Value("${classify.proba}")
    private float classifyProba;

    @Autowired
    public ClassifyServiceImpl(ClassifierWithDeepLearning4j classifier,ModelService modelService) {
        this.classifier = classifier;
//        this.modelService = modelService;
        Model model = modelService.findByEnabled(true);

        if (model != null) {
            ClassifierWithDeepLearning4j.Model.setPath(model.getPath());
            ClassifierWithDeepLearning4j.Model.setHeight(model.getHeight());
            ClassifierWithDeepLearning4j.Model.setWidth(model.getWidth());
            ClassifierWithDeepLearning4j.Model.setChannels(model.getChannels());

            String[] labels = model.getLabels().split(",");
            ClassifierWithDeepLearning4j.Model.setLabels(labels);
        }
        try {
            ClassifierWithDeepLearning4j.init();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public Optional<String> classify(File image) {
        Pair<String, Float> labelWithProba;
        String label = null;
        Float proba;

        try {
            labelWithProba = classifier.classify(image);
            label = labelWithProba.getKey();
            proba = labelWithProba.getValue();
            logger.info("The predict label:" + label);
            logger.info("The predict proba:" + proba.toString());
            if (proba < classifyProba) {
                label = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(label);
    }
}
