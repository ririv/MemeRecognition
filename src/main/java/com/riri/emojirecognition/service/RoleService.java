package com.riri.emojirecognition.service;


import com.riri.emojirecognition.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {

    Role save(Role role);

    Role createRole(String roleName);

    Role findByName(String roleName);

    Role findById(Long roleId);

    List<Role> findAll();

    Page<Role> findAll(Pageable pageable);

}
