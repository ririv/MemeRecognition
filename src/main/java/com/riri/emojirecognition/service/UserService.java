package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;


//业务接口层
public interface UserService {

    List<User> findAll();

    User findById(Long id);

    User findByUsername(String username);

    Page<User> findAll(Pageable pageable);

    User save(User user);

    List<User> saveAll(List<User> users);

    void delete(User user);

    void deleteById(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User updateUserById(Long id,User user);

    User createUser(User user);


}
