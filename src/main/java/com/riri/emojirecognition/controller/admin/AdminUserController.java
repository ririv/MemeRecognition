package com.riri.emojirecognition.controller.admin;

import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.service.UserService;
import com.riri.emojirecognition.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/user")
public class AdminUserController {

    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "operate/{id}")
    public UserVO get(@PathVariable Long id) {
        return new UserVO(userService.findById(id));
    }

    @PutMapping(value = "operate/{id}")
    public UserVO update(@PathVariable Long id, @RequestBody User user) {
        return new UserVO(userService.updateById(id, user));
    }

    @DeleteMapping(value = "operate/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PostMapping(value = "operate")
    public UserVO add(@RequestBody User user) {
        return new UserVO(userService.add(user));
    }

    @GetMapping(value = "query")
    public Page findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PostMapping(value = "create")
    public UserVO create(@RequestBody User transferUser) {
        return new UserVO(userService.createUser(transferUser));
    }

    @GetMapping(value = "search")
    public UserVO find(@RequestParam String username) {
        return new UserVO(userService.findByUsername(username));
    }
}
