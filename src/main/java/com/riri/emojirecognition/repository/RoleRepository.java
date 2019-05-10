package com.riri.emojirecognition.repository;

import com.riri.emojirecognition.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}