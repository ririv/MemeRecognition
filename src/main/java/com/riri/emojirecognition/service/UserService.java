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

    User addUser(User user);

    User addUserRole(User user, Role role);

    User addUserRoleByRoleName(User user, String roleName);

    User addUserRoles(User user, Set<Role> roles);

    User addUserRolesByRoleNames(User user, Set<String> roleNames);

    User updateUserRole(User user, Role role);

    User updateUserRole(User user, String roleName);

    User updateUserRoles(User user, Set<Role> roles);

    User updateUserRolesByRoleNames(User user, Set<String> roleNames);





}
