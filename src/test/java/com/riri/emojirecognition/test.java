package com.riri.emojirecognition;

import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.repository.ImgRepository;
import com.riri.emojirecognition.service.ImgService;
import com.riri.emojirecognition.service.RoleService;
import com.riri.emojirecognition.util.LocalAddressUtil;
import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.repository.RoleRepository;
import com.riri.emojirecognition.repository.UserRepository;
import com.riri.emojirecognition.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@SpringBootTest
public class test {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private ImgRepository imgRepository;


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
        System.out.println("本机地址:"+ LocalAddressUtil.getLocalAddress());

    }

    @Test
    public void test02(){
        roleRepository.save(new Role(1L,"ROLE_ADMIN"));
        roleRepository.save(new Role(2L,"ROLE_USER"));
//        roleRepository.save(new Role(3L,"ROLE_GUEST"));
    }

    //测试批量插入图片
    @Test
    public void test03(){
        String path = "D:\\tests\\12345";
        imgService.batchInsertToDbByDir(path,null,0);
    }

    @Test
//    @Transactional
    public void test04(){
        User user = new User();
        user.setUsername("09813");
        user.setPassword("098");
        userService.createUser(user);
    }


    @Test
    public void test06() {
        String b;
        LinkedList<String> folderList = new LinkedList<>();
        folderList.add("1");
        folderList.add("2");
        folderList.add("3");
        b = folderList.removeFirst();
        for (String a: folderList){
            System.out.println(a);
        }
        System.out.println(b);
    }

    //测试通过标签查询随机图片
    @Test
    public void test07(){
        String tag = "狗";
        List<Img> list;
        list = imgService.findRandomImgsByTagLimitNum(tag ,10);

        System.out.println(list.size());

        for(Img img: list) {
            if (img != null) {
                System.out.println(img.getName());
            }
        }
    }

//    @Test
//    public void test11(){
//        Long minId = imgRepository.findMinId();
//        Long maxId = imgRepository.findMaxId();
//        Long range = maxId - minId;
//
//        Long randomId = 1L;
//        List<Img> randomImgs = new ArrayList<>();
//        for (int i = 0; i<10; i++){
//            Optional<Img> img = imgRepository.findById(randomId);
//            if (img.isPresent()){
//            Img img2 = img.get();
//            }
//        }
//    }

    @Test
    public void test12(){
        Long max = imgRepository.findMaxId();
        Long min = imgRepository.findMinId();
        Long randomNum = ThreadLocalRandom.current().nextLong(min, max + 1);
        System.out.println(randomNum);

    }

    @Test
    public void test13(){
        Set<Long> randomNumSet = new HashSet<>();
        long randomNum;
        while (randomNumSet.size() < 10){
            randomNum = ThreadLocalRandom.current().nextLong(0, 10);
            randomNumSet.add(randomNum);
            System.out.println(randomNumSet);
        }

        System.out.println(randomNumSet);

    }

    @Test
    public void test14(){
        long count;
        count = imgRepository.countByTag("狗");
        System.out.println(count);

    }

    @Test
    public void test15(){
        User user = new User();
        if(user.getId()==null){
            System.out.println(true);
        }
    }

}