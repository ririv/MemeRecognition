package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

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


    @Test
    public void test01() {
        User user = new User();

        user.setId(16L);
        user.setPassword("111");
        user.setUsername("111");
        userService.updateById(16L,user);
//        System.out.println(Arrays.toString(field));
    }

    @Test
    public void getValue(){
        User user = new User();
        user.setId(202L);
        user.setPassword("123456");

        Field[] fields = user.getClass().getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();

            name = name.substring(0, 1).toUpperCase() + name.substring(1);//将属性第一个字母大写，用于获得get方法

            if (field.getGenericType() == String.class) {
                Method m;
                String value;
                try {
                    m = user.getClass().getMethod("get" + name); //获得get方法
                    value = (String) m.invoke(user);
                    if (value != null && !"".equals(value)) {//如果属性不为null或者空字符串
                        System.out.println(name);
                        System.out.println(value);
                    }
                } catch (IllegalAccessException | SecurityException | NoSuchMethodException | InvocationTargetException | IllegalArgumentException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Test
    public void test02(){
        System.out.println(userService.findAll(Pageable.unpaged()).getContent().get(0).getRoleNames());
    }

    @Test
    public void test03(){
        System.out.println(userService.findAll().get(0).getRoleNames());
    }

}