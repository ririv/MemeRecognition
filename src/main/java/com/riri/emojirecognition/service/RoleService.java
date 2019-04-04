package com.riri.emojirecognition.service;


import com.riri.emojirecognition.domain.Role;

import java.util.List;

public interface RoleService {

    Role save(Role role);

    Role createRole(String roleName);

    Role findByName(String roleName);

    Role findById(Long roleId);

    List<Role> findAll();

}
