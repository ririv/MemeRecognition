package com.riri.emojirecognition.service.impl;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.repository.RoleRepository;
import com.riri.emojirecognition.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role){return roleRepository.save(role);}

    @Override
    public Role createUser(String roleName){
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    @Override
    public Role findByName(String roleName){
        return roleRepository.findByName(roleName);
    }

    @Override
    public List<Role> findAll(){
        return roleRepository.findAll();
    }
}
