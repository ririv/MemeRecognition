package com.riri.emojirecognition.service.impl;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.exception.notfound.RoleIsNotPresentException;
import com.riri.emojirecognition.repository.RoleRepository;
import com.riri.emojirecognition.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Role createRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    public Role findByName(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new RoleIsNotPresentException("role name: " + roleName);
        }
        return role;
    }

    public Role findById(Long roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (!optionalRole.isPresent()) {
            throw new RoleIsNotPresentException("id: " + roleId);
        }
        return optionalRole.get();
    }


    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }
}
