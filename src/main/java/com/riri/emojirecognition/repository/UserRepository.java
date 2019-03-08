package com.riri.emojirecognition.repository;

import com.riri.emojirecognition.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//数据持久层操作接口

public interface UserRepository extends
        JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    User findByEmail(String email);

}