package com.riri.emojirecognition.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImgServiceTest {
    @Autowired
    ImgService imgService;

    @Test
    public void findAll() {
        int a =1;
        int b=10;
        Page page= imgService.findAll(a,b);
        System.out.println(page);

    }
}