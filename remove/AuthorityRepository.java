package com.riri.emojirecognition.repository;

import com.riri.emojirecognition.domain.Authority;
import com.riri.emojirecognition.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthorityRepository extends
        JpaRepository<Authority, Long> {

}