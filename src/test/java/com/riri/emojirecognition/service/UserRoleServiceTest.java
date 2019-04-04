package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRoleServiceTest {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    //测试批量添加角色
    @Test
//    @Transactional
    public void test05(){
        List<String> roleNames = new ArrayList<>();
        roleNames.add("ROLE_ADMIN");
        roleNames.add("ROLE_GUEST");
        roleNames.add("ROLE_USER");
        User user = userRoleService.addUserRoleByRoleName(userService.findById(70L), roleNames);
        System.out.println(user.getId());
    }

    //测试增加角色 Role类型
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
        userRoleService.addUserRole(user,role);

    }

    //测试修改角色 Role类型
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

        User user = userService.findById(70L);
        userRoleService.updateUserRole(user,allRoles);
    }

    //测试批量修改角色 String类型
    @Test
//    @Transactional
    public void test10(){
        List<String> roleNames = new ArrayList<>();
        roleNames.add("ROLE_ADMIN");
        roleNames.add("ROLE_GUEST");
//        roleNames.add("ROLE_USER");
        User user = userRoleService.updateUserRoleByRoleName(userService.findById(70L), roleNames);
        System.out.println(user.getId());
    }

    //测试批量修改角色 Long类型
    @Test
//    @Transactional
    public void test11(){
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        User user = userRoleService.updateUserRoleByRoleId(userService.findById(70L), roleIds);
        System.out.println(user.getId());
    }
}