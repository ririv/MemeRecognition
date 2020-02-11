package com.riri.memerecognition.controller;


import com.riri.memerecognition.domain.User;
import com.riri.memerecognition.service.UserService;
import com.riri.memerecognition.vo.UserVO;
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
    public UserVO find(@PathVariable Long id) {
        return new UserVO(userService.findById(id));
    }

    @PostMapping("/create")
    public UserVO create(@RequestBody User transferUser) {
        return new UserVO(userService.createUser(transferUser));
    }

//    @RequestMapping(value="/logout", method = RequestMethod.GET)
//    public ResponseEntity logout (HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return ResponseEntity.ok("logout successfully");
//    }

    @PutMapping("change-password")
    public void updatePassword(String username,String password){
        userService.changePassword(username,password);
    }
}
