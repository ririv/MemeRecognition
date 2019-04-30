package com.riri.emojirecognition.controller.admin;

import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.service.UserService;
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
    public User get(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping(value = "operate/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        return userService.updateById(id, user);
    }

    @DeleteMapping(value = "operate/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PostMapping(value = "create")
    public User create(@RequestBody User transferUser) {
        return userService.createUser(transferUser);
    }

    @GetMapping(value = "query")
    public Page findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping(value = "search")
    public User find(@RequestParam String username) {
        return userService.findByUsername(username);
    }
}
