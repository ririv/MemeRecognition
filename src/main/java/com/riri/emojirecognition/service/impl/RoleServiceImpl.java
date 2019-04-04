package com.riri.emojirecognition.service.impl;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.exception.RoleIsNotPresentException;
import com.riri.emojirecognition.repository.RoleRepository;
import com.riri.emojirecognition.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Role save(Role role){return roleRepository.save(role);}

    @Override
    public Role createRole(String roleName){
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    @Override
    public Role findByName(String roleName){
        return roleRepository.findByName(roleName);
    }

    @Override
    public Role findById(Long roleId){
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (!optionalRole.isPresent()){
            throw new RoleIsNotPresentException();
        }
        return optionalRole.get();
    }

    @Override
    public List<Role> findAll(){
        return roleRepository.findAll();
    }
}
