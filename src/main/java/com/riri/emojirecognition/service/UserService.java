package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.User;

import java.util.List;


//业务接口层
public interface UserService {

    List<User> findAll();

    User findById(Long id);

    User save(User user);

    void deleteById(Long id);

    boolean emailExist(String email);

    boolean usernameExist(String username);

    void register(User user);

}
