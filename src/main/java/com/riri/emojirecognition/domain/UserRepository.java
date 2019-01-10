package com.riri.emojirecognition.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface UserRepository extends
        Serializable,
        JpaRepository<User, Integer>,
        JpaSpecificationExecutor<User> {
}