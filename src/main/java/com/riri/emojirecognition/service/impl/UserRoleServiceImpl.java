package com.riri.emojirecognition.service.impl;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.repository.UserRepository;
import com.riri.emojirecognition.service.RoleService;
import com.riri.emojirecognition.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    @Autowired
    public UserRoleServiceImpl(RoleService roleService, UserRepository userRepository) {
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    /**
     * 经测试，使用list时，在增加或修改单个用户权限时，即使权限一样，数据库也会重新删除权限，再插入相同的权限
     * 但使用set时，数据库会查询权限，对比权限是否相同，如果相同则不继续往下操作
     * @param user 需要操作的用户
     * @param role 需要增加的权限
     * @return user
     */
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

    //增加单个角色
    @Override
    public User addUserRoleByRoleId(User user, Long roleId){
        Role role = roleService.findById(roleId);
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User addUserRole(User user, List<Role> roles) {
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
    public User addUserRoleByRoleName(User user, List<String> roleNames) {
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

    //增加多个角色
    @Override
    public User addUserRoleByRoleId(User user, List<Long> roleIds) {


        //获得原始权限
        Set<Role> oldRoles = user.getRoles();
        Set<Role> newRoles = new HashSet<>();
        Role role;

        //组合旧权限与要新增的权限，使用set避免重复数据
        for (Role r : oldRoles) {
            roleIds.add(r.getId());
        }

        //遍历组合后的Set权限集，添加至空权限集newRoles
        for (Long roleId : roleIds) {
            role = roleService.findById(roleId);
            newRoles.add(role);
        }
        user.setRoles(newRoles);
        return userRepository.save(user);
    }

    /**
     * 因为User中使用的Set保存Role
     * 所以所有数据完全一样则不操作
     * 相同的数据也不会操作
     * 少的数据会插入，多的数据会删除
     * 这样可以减小数据库的压力
     */

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
    public User updateUserRoleByRoleName(User user, String roleName) {
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

    @Override
    public User updateUserRoleByRoleId(User user, Long roleId){
        Role role = roleService.findById(roleId);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user = userRepository.save(user);
        return user;
    }

    //更新多个角色
    @Override
    public User updateUserRole(User user, List<Role> roles) {
        Set<Role> newRoles = new HashSet<>(roles);
        user.setRoles(newRoles);
        return userRepository.save(user);
    }

    //更新多个角色
    @Override
    public User updateUserRoleByRoleName(User user, List<String> roleNames) {
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

    //实际业务中可能只用到这个
    @Override
    public User updateUserRoleByRoleId(User user, List<Long> roleIds) {
        Set<Role> newRoles = new HashSet<>();
        Role role;
        for (Long roleId : roleIds) {
            role = roleService.findById(roleId);
            newRoles.add(role);
        }
        user.setRoles(newRoles);
        return userRepository.save(user);
    }
}
