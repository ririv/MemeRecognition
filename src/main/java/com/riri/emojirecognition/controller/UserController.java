package com.riri.emojirecognition.controller;


import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


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

//    @RequestMapping("/current2")
//    public Object user() {
//        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();//获取spring security封装的当前用户信息对象
//    }

    @GetMapping("details/{id}")
    public User findImg(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/create")
    public User register(User transferUser) {
         return userService.createUser(transferUser);
    }

//    @RequestMapping(value="/logout", method = RequestMethod.GET)
//    public ResponseEntity logout (HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return ResponseEntity.ok("logout successfully");
//    }

    @PutMapping("update/password")
    public void updatePassword(String username,String password){
        userService.updatePassword(username,password);
    }
}
