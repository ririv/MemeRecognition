package com.riri.emojirecognition.web.controller;


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

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/admin")
    public String printAdminRole() {
        return "如果你看见这句话，说明你访问/admin路径具有ADMIN权限";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/user")
    public String printUserRole() {
        return "如果你看见这句话，说明你访问/user路径具有USER权限";
    }

    @GetMapping("/login")
    public ModelAndView index() {
        //ModelAndView mv = new ModelAndView("signin");
        //return mv;
        //return new ModelAndView("signin");
        return new ModelAndView("signin.html");
    }

//    @RequestMapping("/user")
////    当前用户信息
//    public Object user(Authentication authentication) {
//        return authentication.getPrincipal();
//    }


//    @PostMapping("/registry")
//    public void registry(User user) {
//        if (userService.usernameExist(user.getUsername())){
//            System.out.println("用户名已存在");
//            throw new UserAlreadyExistException("There is an account with that email address: " + user.getEmail());
//        }
//
//        User newUser = new User();
//        newUser.setUsername(user.getUsername());
//        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
//        newUser.setEmail(user.getEmail());
//        newUser.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
//        userRepository.save(newUser);
//    }

    @PostMapping("/register")
    public void registry(User user) {
        userService.register(user);
    }

    @RequestMapping("/register")
    public ModelAndView registry() {
        return new ModelAndView("signup.html");

    }

    @RequestMapping(value = "api/json/user/{name}", method = RequestMethod.GET)
    public User getUser(@PathVariable String name) {
        User user = new User();
        user.setUsername(name);
        user.setEmail("123");
        return user;
    }

//    @GetMapping(value = "api/json/manage/user/role")
//    public

}
