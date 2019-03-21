package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.domain.User;

import java.util.List;
import java.util.Set;


//业务接口层
public interface UserService {

    List<User> findAll();

    User findById(Long id);

    User save(User user);

    List<User> saveAll(List<User> users);

    void deleteById(Long id);

    boolean emailExist(String email);

    boolean usernameExist(String username);

    User register(User user);

    User addRole(User user, Role role);

    User addRole2(User user, String roleName);

    User addRoles(User user, Set<Role> roles);

    User addRoles2(User user, Set<String> roleNames);

    User updateRole(User user, Role role);

    User updateRole2(User user, String roleName);

    User updateRoles(User user, Set<Role> roles);

    User updateRoles2(User user, Set<String> roleNames);





}
