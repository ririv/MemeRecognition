package com.riri.emojirecognition.controller;


import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/admin")
    public String printAdminRole() {
        return "如果你看见这句话，说明你具有ADMIN权限";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/user")
    public String printUserRole() {
        return "如果你看见这句话，说明你具有USER权限";
    }

    @GetMapping("/login")
    public ModelAndView index() {
        //ModelAndView mv = new ModelAndView("signin");
        //return mv;
        //return new ModelAndView("signin");
        return new ModelAndView("signin.html");
    }

    @RequestMapping("/api/user/current")
//    当前用户信息
    public Object user(Authentication authentication) {
        return authentication.getPrincipal();
    }


    @PostMapping("/register")
    public void register(User user) {
        userService.addUser(user);
    }

    @RequestMapping("/register")
    public ModelAndView registry() {
        return new ModelAndView("signup.html");
    }

    @RequestMapping(value = "api/user/{username}", method = RequestMethod.GET)
    public User getUser(@PathVariable String username) {
        User user = new User();
        user.setUsername(username);
        return user;
    }

//    @GetMapping(value = "api/json/manage/user/role")
//    public

}
