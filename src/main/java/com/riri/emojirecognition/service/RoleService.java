package com.riri.emojirecognition.service;


import com.riri.emojirecognition.domain.Role;

import java.util.List;

public interface RoleService {

    Role save(Role role);

    Role createUser(String roleName);

    Role findByName(String roleName);

    List<Role> findAll();

}
