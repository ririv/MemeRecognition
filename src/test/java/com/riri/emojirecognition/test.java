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
    private RoleService roleService;

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
        imgService.batchInsertToDbByFolder(path,null);
    }

    @Test
//    @Transactional
    public void test04(){
        User user = new User();
        user.setUsername("09813");
        user.setPassword("098");
        userService.register(user);
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
        String tag = "123";
        List<Img> list;
        list = imgService.findRandomImgsByTagLimitNum(tag ,2);

//        System.out.println(list);

//        list = imgRepository.findRandomImgByTab(2);
        for(Img img: list){
            System.out.println(img.getName());
        }
    }

    //测试批量添加角色
    @Test
//    @Transactional
    public void test05(){
        Set<String> roleNames = new HashSet<>();
        roleNames.add("ROLE_ADMIN");
        roleNames.add("ROLE_GUEST");
        roleNames.add("ROLE_USER");
        User user = userService.addRoles2(userService.findById(70L), roleNames);
        System.out.println(user.getId());
    }

    //测试增加角色 role类型
    @Test
//    @Transactional
    public void test08(){
        List<Role> allRoles = roleService.findAll();
        List<String> namesOfAllRoles = new ArrayList<>();
        int i = 0;
        for (Role role: allRoles){
            namesOfAllRoles.add(role.getName());
            System.out.println(i + "\t" + role.getName());
            i++;
        }
        Role role = allRoles.get(1);
        User user = userService.findById(70L);
        userService.addRole(user,role);

    }

    //测试修改角色 role类型
    @Test
//    @Transactional
    public void test09(){
        List<Role> allRoles = roleService.findAll();
        List<String> namesOfAllRoles = new ArrayList<>();
        int i = 0;
        for (Role role: allRoles){
            namesOfAllRoles.add(role.getName());
            System.out.println(i + "\t" + role.getName());
            i++;
        }
        Set<Role> roles = new HashSet<>(allRoles);
        System.out.println(roles);
        for (Role role: roles){
            System.out.println(i + "\t" + role.getName());
        }

        User user = userService.findById(70L);
        userService.updateRoles(user,roles);
    }

    //测试批量修改角色 string类型
    @Test
//    @Transactional
    public void test10(){
        Set<String> roleNames = new HashSet<>();
        roleNames.add("ROLE_ADMIN");
        roleNames.add("ROLE_GUEST");
//        roleNames.add("ROLE_USER");
        User user = userService.updateRoles2(userService.findById(70L), roleNames);
        System.out.println(user.getId());
    }

    @Test
    public void test11(){
        Arrays.asList( "a", "b", "d" ).forEach( e ->
                System.out.print( e )

        );
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

}