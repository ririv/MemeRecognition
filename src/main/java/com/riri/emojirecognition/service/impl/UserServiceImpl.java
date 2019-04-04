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
import org.springframework.data.domain.Pageable;
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
    public UserServiceImpl(PasswordEncoder passwordEncoder,RoleService roleService,UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()){
            throw new UserNotFoundException();
        }
        return optionalUser.get();
    }

    @Override
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<User> findAll(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> saveAll(List<User> users) {
        return userRepository.saveAll(users);
    }

    @Override
    public void delete(User user){
        userRepository.delete(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 通过id来修改用户信息
     * 因为这里是通过id来修改用户信息
     * 所以由所指定的id来确定user，而不是直接通过user实例来确定
     * 防止发生id的错乱
     * @param id 指定的id
     * @param user 用户信息
     * @return user
     */
    @Override
    public User updateUserById(Long id,User user){

        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()){
            throw new UserNotFoundException("The User not found:"+id);
        }
        else {
            //设置id为所指定的id，防止user中有id信息，而发生更新错位的现象
            user.setId(id);
            user.setPassword(user.getPassword());
            userRepository.save(user);
            return user;
        }
    }

    @Override
    public User createUser(User user) {

        if (existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException("The username already exists: " + user.getUsername());
        }
        if (user.getEmail()!=null) { //不加此判定，邮箱为空时也会抛出异常
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

