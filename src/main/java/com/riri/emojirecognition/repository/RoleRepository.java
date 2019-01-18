package com.riri.emojirecognition.repository;

import com.riri.emojirecognition.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface RoleRepository extends
        JpaRepository<Role, Long>,
        JpaSpecificationExecutor<Role> {
        Role findByName(String name);

}