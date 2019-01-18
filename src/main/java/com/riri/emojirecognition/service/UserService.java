package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.User;

import java.util.List;


//业务接口层
public interface UserService {

    //获取所有 User
    List<User> findAll();

    //获取 User
    //@param id 编号
    User findById(Long id);

    //新增或更新 User
    //@param user {@link User}
    User save(User user);

    //删除 User
    //@param id 编号
    void deleteById(Long id);

    boolean emailExist(String email);

    boolean usernameExist(String username);

    void register(User user);

}
