package com.riri.emojirecognition.service.impl;

import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.repository.RoleRepository;
import com.riri.emojirecognition.repository.UserRepository;
import com.riri.emojirecognition.service.UserService;
import com.riri.emojirecognition.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

//业务层实现
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public boolean usernameExist(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public void register(User user){

        if (usernameExist(user.getUsername())){
            System.out.println("用户名已存在");
            throw new UserAlreadyExistException("There is an account with the username: " + user.getUsername());
        }

        userRepository.save(new User(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getEmail(),
                Collections.singletonList(roleRepository.findByName("ROLE_USER"))
                ));
    }
}

