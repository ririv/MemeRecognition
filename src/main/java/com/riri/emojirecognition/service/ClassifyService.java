package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.Model;
import javafx.util.Pair;

import java.io.File;
import java.util.Optional;

public interface ClassifyService {

    void init(Model model,int flag);

    void init(Long id,int flag);

    Optional<Pair<String, Float>> classify(File image,int flag);

    void enableModelById(Long id, int flag);


}
