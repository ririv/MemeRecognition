package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.vo.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


//业务接口层
public interface UserService {

    List<UserVO> findAll();

    Page<UserVO> findAll(int page, int size);

    Page<UserVO> findAll(int page, int size, Sort sort);

    Page<UserVO> findAll(int page, int size, Sort.Direction direction,String... properties);

    Page<UserVO> findAll(Pageable pageable);

    User findById(Long id);

    User findByUsername(String username);

    User save(User user);

    List<User> saveAll(List<User> users);

    void delete(User user);

    void deleteById(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User updateById(Long id, User user);

    User updatePassword(Long id, String password);

    User updatePassword(String username, String password);

    User createUser(User user);


}
