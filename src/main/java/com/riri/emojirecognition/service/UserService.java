package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;


//业务接口层
public interface UserService {

    List<User> findAll();

    Page<User> findAll(int page, int size);

    Page<User> findAll(int page, int size, Sort sort);

    Page<User> findAll(int page, int size, Sort.Direction direction,String... properties);

    Page<User> findAll(Pageable pageable);

    User findById(Long id);

    User findByUsername(String username);

    User save(User user);

    List<User> saveAll(List<User> users);

    void delete(User user);

    void deleteById(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User updateUserById(Long id,User user);

    User updatePasswordById(Long id, String password);

    User createUser(User user);


}
