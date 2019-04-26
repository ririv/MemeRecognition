package com.riri.emojirecognition.service;

import com.riri.emojirecognition.repository.ImgRepository;
import com.riri.emojirecognition.service.impl.ImgServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImgServiceImplImplTest {
    @Autowired
    ImgServiceImpl imgServiceImpl;

    @Autowired
    ImgRepository imgRepository;

    @Test
    public void test01() {
        int a =1;
        int b=10;
        Page page= imgServiceImpl.findAll(a,b);
        System.out.println(page);

    }

    @Test
    public void test02() {
        Page page = imgRepository.findByTag("猫", Pageable.unpaged());
        System.out.println(page.getContent());
    }
}