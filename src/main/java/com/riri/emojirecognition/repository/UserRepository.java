package com.riri.emojirecognition.repository;

import com.riri.emojirecognition.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface UserRepository extends
        Serializable,
        JpaRepository<User, Integer>,
        JpaSpecificationExecutor<User> {
    User findByUsername(String username);
}