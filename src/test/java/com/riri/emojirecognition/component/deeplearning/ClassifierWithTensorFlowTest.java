package com.riri.emojirecognition.component.deeplearning;

import org.junit.Test;

public class ClassifierWithTensorFlowTest {
    @Test
        public void test01(){
        ClassifierWithTensorFlow classifierWithTensorFlow = new ClassifierWithTensorFlow();
        classifierWithTensorFlow.classify();
    }
}