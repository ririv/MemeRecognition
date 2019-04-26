package com.riri.emojirecognition.service.impl;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.exception.UserNotFoundException;
import com.riri.emojirecognition.repository.UserRepository;
import com.riri.emojirecognition.service.RoleService;
import com.riri.emojirecognition.service.UserService;
import com.riri.emojirecognition.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

//业务层实现
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, RoleService roleService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    public Page<User> findAll(int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    public Page<User> findAll(int page, int size, Sort.Direction direction, String... properties) {
        Pageable pageable = PageRequest.of(page, size, direction, properties);
        return userRepository.findAll(pageable);
    }

    
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException("The user is not found, " + "user id: " + id);
        }
        return user.get();
    }

    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    
    public User save(User user) {
        return userRepository.save(user);
    }

    
    public List<User> saveAll(List<User> users) {
        return userRepository.saveAll(users);
    }

    
    public void delete(User user) {
        userRepository.delete(user);
    }

    
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 通过id来修改用户信息
     * 因为这里是通过id来修改用户信息
     * 所以由所指定的id来确定user，而不是直接通过user实例来确定
     * 防止发生id的错乱
     *
     * @param id   指定的id
     * @param user 用户信息
     * @return user
     */
    
    public User updateById(Long id, User user) {

        findById(id);//如果此用户不存在则会抛出异常
        //设置id为所指定的id，防止user中有id信息，而发生更新错位的现象
        user.setId(id);

        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    
    public User updatePasswordById(Long id, String password){

        User user =  findById(id);//如果此用户不存在则会抛出异常
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    
    public User createUser(User user) {

        if (existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException("The username already exists: " + user.getUsername());
        }
        if (user.getEmail() != null) { //不加此判定，邮箱为空时也会抛出异常
            if (existsByEmail(user.getEmail())) {
                throw new UserAlreadyExistException("The email already exists: " + user.getEmail());
            }
        }

        //测试，这里Set集合不能删除同id的role，因为使用的add，函数的hashCode不一样
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByName("ROLE_USER"));
//        roles.add(roleService.findByName("ROLE_ADMIN"));
//        roles.add(roleService.findByName("ROLE_ADMIN"));
//        for (Role role: roles){
//        System.out.println(role.hashCode());}

        user.setRoles(roles);
//                    Collections.singletonList(roleService.findByName("ROLE_USER"))
//                    Arrays.asList(roleRepository.findByName("ROLE_USER"),roleRepository.findByName("ROLE_ADMIN")) //此方法可添加多个权限
        user.setPassword(passwordEncoder.encode(user.getPassword())); //加密密码
        user.setId(null); //将id设置为空，id将自增，防止通过id修改信息
        return userRepository.save(user);
    }

}

