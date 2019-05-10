package com.riri.emojirecognition.service;

import com.riri.emojirecognition.component.deeplearning.ClassifierWithDeepLearning4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassifyServiceTest {

    @Autowired
    ClassifyService classifyService;

    @Autowired
    ClassifierWithDeepLearning4j classifier;

//    @Test
//    public void test01(){
//        classifyService.enableModelById(2828L,1);
//        classifyService.enableModelById(24209L,1);
//        System.out.println(Arrays.toString(classifier.getSelectedModelLabels()));
//    }

}