package com.riri.emojirecognition.service.impl;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.repository.UserRepository;
import com.riri.emojirecognition.service.RoleService;
import com.riri.emojirecognition.service.UserService;
import com.riri.emojirecognition.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

//业务层实现
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder,RoleService roleService,UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> saveAll(List<User> users) {
        return userRepository.saveAll(users);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public boolean usernameExist(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public User addUser(User user) {

        if (usernameExist(user.getUsername())) {
//            System.out.println("用户名已存在");
            throw new UserAlreadyExistException("There is an account with the username: " + user.getUsername());
        }
        //测试，这里不能删除同id的role，因为使用的add，函数的hashCode不一样
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByName("ROLE_USER"));
//        roles.add(roleService.findByName("ROLE_ADMIN"));
//        roles.add(roleService.findByName("ROLE_ADMIN"));
//        for (Role role: roles){
//        System.out.println(role.hashCode());}
        return userRepository.save(
                new User(
                        user.getUsername(),
                        passwordEncoder.encode(user.getPassword()),
                        user.getEmail(),
                        roles
//                        Collections.singletonList(roleService.findByName("ROLE_USER"))
//                        Arrays.asList(roleRepository.findByName("ROLE_USER"),roleRepository.findByName("ROLE_ADMIN")) //此方法可添加多个权限
                )
        );
    }

    //经测试，使用list时，在增加或修改单个用户权限时，即使权限一样，数据库也会重新删除权限，再插入相同的权限
    //但使用set时，数据库会查询权限，对比权限是否相同，如果相同则不继续往下操作
    @Override
    public User addUserRole(User user, Role role) {
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        user = userRepository.save(user);
        return user;
    }

    //增加单个角色
    @Override
    public User addUserRoleByRoleName(User user, String roleName) {
//        查询有无此角色，因为修改角色改为选择式，这里不再验证角色是否为null
        Role role = roleService.findByName(roleName);
//        if (role != null) {
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        user = userRepository.save(user);
//        }else {System.out.println("无此角色:"+roleName);}
        return user;
    }

    @Override
    public User addUserRoles(User user, Set<Role> roles) {
        //获得原始权限
        Set<Role> oldRoles = user.getRoles();

        //组合旧权限与要新增的权限，使用set避免重复数据
        roles.addAll(oldRoles);
        Set<Role> newRoles = new HashSet<>(roles);
        user.setRoles(newRoles);
        return userRepository.save(user);
    }

    //增加多个角色
    @Override
    public User addUserRolesByRoleNames(User user, Set<String> roleNames) {
        //获得原始权限
        Set<Role> oldRoles = user.getRoles();
        Set<Role> newRoles = new HashSet<>();

        //组合旧权限与要新增的权限，使用set避免重复数据
        for (Role role : oldRoles) {
            roleNames.add(role.getName());
        }

        //遍历组合后的Set权限集，添加至空权限集newRoles
        for (String roleName : roleNames) {
//        查询有无此角色，因为修改角色改为选择式，这里不再验证角色是否为null
            Role role = roleService.findByName(roleName);
//            if (role != null){
            newRoles.add(role);
//            }else {System.out.println("无此角色:"+roleName);}
        }
        user.setRoles(newRoles);
        return userRepository.save(user);
    }


    //更新单个角色
    @Override
    public User updateUserRole(User user, Role role) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user = userRepository.save(user);
//        }else {System.out.println("无此角色:"+roleName);}
        return user;
    }

    //更新单个角色
    @Override
    public User updateUserRole(User user, String roleName) {
//        查询有无此角色，因为修改角色改为选择式，这里不再验证角色是否为null
        Role role = roleService.findByName(roleName);
//        if (role != null) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user = userRepository.save(user);
//        }else {System.out.println("无此角色:"+roleName);}
        return user;
    }

    //更新多个角色
    @Override
    public User updateUserRoles(User user, Set<Role> roles) {
        user.setRoles(roles);
        return userRepository.save(user);
    }

    //更新多个角色
    @Override
    public User updateUserRolesByRoleNames(User user, Set<String> roleNames) {
        Set<Role> newRoles = new HashSet<>();
        for (String roleName : roleNames) {
//        查询有无此角色，因为修改角色改为选择式，这里不再验证角色是否为null
            Role role = roleService.findByName(roleName);
//            if (role != null) {
            newRoles.add(role);
//            }else {System.out.println("无此角色:"+roleName);}
        }
        user.setRoles(newRoles);
        return userRepository.save(user);
    }

}

