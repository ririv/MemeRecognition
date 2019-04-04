package com.riri.emojirecognition.controller;


import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/current")
//    当前用户信息
    public Object user(Authentication authentication) {
        return authentication.getPrincipal();
    }

    @PostMapping("/create")
    public User register(User user) {
         return userService.createUser(user);
    }

}
