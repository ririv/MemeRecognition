package com.riri.emojirecognition.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void existsByEmail() {
        System.out.println(userService.existsByEmail("123"));
    }

    @Test
    public void existsByUsername() {
        System.out.println(userService.existsByUsername("111"));
    }
}