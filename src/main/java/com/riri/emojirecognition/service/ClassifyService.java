package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.Model;
import javafx.util.Pair;

import java.io.File;
import java.util.Map;
import java.util.Optional;

public interface ClassifyService {

    void init(Model model,int flag);

    void init(Long id,int flag);

    Optional<Pair<String, Float>> classify(File image,int flag);

    void enableModelById(Long targetModelId, int flag);

    void enableModel(Model targetModel, int flag);

    Map<String,Object> getCurrentModelsInfoOfClassifier();


}
