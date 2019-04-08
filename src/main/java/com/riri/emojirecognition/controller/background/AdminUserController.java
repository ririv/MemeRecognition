package com.riri.emojirecognition.controller.background;

import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/user")
public class AdminUserController {

    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "operate/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping(value = "operate/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUserById(id, user);
    }

    @DeleteMapping(value = "operate/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PostMapping(value = "create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping(value = "query")
    public Page findAll(@PageableDefault(value = 20, sort = {"tag"}) Pageable pageable) {
        return userService.findAll(pageable);
    }
}
