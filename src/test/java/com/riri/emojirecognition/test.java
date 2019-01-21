package com.riri.emojirecognition;

import com.riri.emojirecognition.utils.LocalAddressUtils;
import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.repository.RoleRepository;
import com.riri.emojirecognition.repository.UserRepository;
import com.riri.emojirecognition.service.UserService;
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

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    //    @Test
//    public void contextLoads() throws Exception {}
//        //User user = new User();
//        userRepository.findAll();
//        userRepository.save(new User("1", "20", "123@qq.com"));
//        //userRepository.delete(user);
//        userRepository.count();
//        // 测试findAll, 查询所有记录
//        Assert.assertEquals("错误",10, userRepository.findAll().size());
//
//        // 测试findByName
//        Assert.assertEquals("错误",60, userRepository.findByUsername("1"));
//
//        // 测试删除姓名为AAA的User
//        userRepository.delete(userRepository.findByUsername("1"));
//
//        // 测试findAll, 查询所有记录, 验证上面的删除是否成功
//        Assert.assertEquals(9, userRepository.findAll().size());
//
//
//        userRepository.findByUsername("testName");
//
//    }
//    @Test
//    public void PasswordEncoder() {
//        User user = userRepository.findByUsername("1234");
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        // 加密
//        String encodedPassword = passwordEncoder.encode(user.getPassword().trim());
//        user.setPassword(encodedPassword);
//        userRepository.save(user);
//    }

    @Test
    public void delete() {
        userRepository.deleteById(15L);
    }


    @Test
    public void test01(){
//        userRepository.save(new User("1", "20", "123@qq.com", Collections.singletonList(roleRepository.findByName("ROLE_USER"))));
        System.out.println("本机地址:"+LocalAddressUtils.getLocalAddress());

    }

    @Test
    public void test02(){
        roleRepository.save(new Role(1L,"ROLE_ADMIN"));
        roleRepository.save(new Role(2L,"ROLE_USER"));
    }
}