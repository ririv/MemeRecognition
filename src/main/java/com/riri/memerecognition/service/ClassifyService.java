package com.riri.memerecognition.service;

import com.riri.memerecognition.domain.Model;
import com.riri.memerecognition.util.Pair;

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
