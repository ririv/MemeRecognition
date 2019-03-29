package com.riri.emojirecognition.component.deeplearning;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

public class ClassifierWithDeepLearning4JTest {

    @Autowired
    ClassifierWithDeepLearning4j classifier;

    @Test
    public void classify() throws Exception{

        File file = new File("D:\\tests\\1.jpg");
        classifier.classify(file);
    }
}