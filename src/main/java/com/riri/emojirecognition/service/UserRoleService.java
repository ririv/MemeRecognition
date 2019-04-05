package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.domain.User;

import java.util.List;

public interface UserRoleService {

    User addUserRole(User user, Role role);

    User addUserRoleByRoleName(User user, String roleName);

    User addUserRoleByRoleId(User user, Long roleId);

    User addUserRole(User user, List<Role> roles);

    User addUserRoleByRoleName(User user, List<String> roleNames);

    User addUserRoleByRoleId(User user, List<Long> roleIds);

    User updateUserRole(User user, Role role);

    User updateUserRoleByRoleName(User user, String roleName);

    User updateUserRoleByRoleId(User user, Long roleId);

    User updateUserRole(User user, List<Role> roles);

    User updateUserRoleByRoleName(User user, List<String> roleNames);

    User updateUserRoleByRoleId(User user, List<Long> roleIds);

}
