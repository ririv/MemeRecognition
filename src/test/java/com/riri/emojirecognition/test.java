package com.riri.emojirecognition;

import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class test {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() throws Exception {
        User user = new User();
        userRepository.findAll();
        userRepository.save(new User("1", "20", 4));
        userRepository.delete(user);
        userRepository.count();
        // 测试findAll, 查询所有记录
        Assert.assertEquals("错误",10, userRepository.findAll().size());

        // 测试findByName
        Assert.assertEquals("错误",60, userRepository.findByUsername("1").getId().longValue());

        // 测试删除姓名为AAA的User
        userRepository.delete(userRepository.findByUsername("1"));

        // 测试findAll, 查询所有记录, 验证上面的删除是否成功
        Assert.assertEquals(9, userRepository.findAll().size());


        userRepository.findByUsername("testName");

    }
}