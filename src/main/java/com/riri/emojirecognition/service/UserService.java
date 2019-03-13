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

    User addRole(User user, String roleName);

    User addRole(User user, Role role);

    User addRoles(User user, Set<String> roleNames);

    User addRoles2(User user, Set<Role> roles);

    User updateRole(User user, String roleName);

    User updateRole(User user, Role role);

    User updateRoles(User user, Set<String> roleNames);

    User updateRoles(User user, List<Role> roles);



}
