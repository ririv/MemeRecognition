package com.riri.emojirecognition.controller.background;

import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.service.UserRoleService;
import com.riri.emojirecognition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/user")
public class AdminUserRoleController {

    private final UserService userService;

    private final UserRoleService userRoleService;

    @Autowired
    public AdminUserRoleController(UserService userService,UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @PatchMapping(value="/{userId}/role/add/one/rolename")
    public User addUserRoleByRoleName(@PathVariable Long userId, String roleName){
        User user = userService.findById(userId);
        return userRoleService.addUserRoleByRoleName(user,roleName);
    }

    @PatchMapping(value="/{userId}/role/add/one/")
    public User addUserRoleByRoleName(@PathVariable Long userId, Long roleId){
        User user = userService.findById(userId);
        return userRoleService.addUserRoleByRoleId(user,roleId);
    }

    @PatchMapping(value="/{userId}/role/update/one/rolename")
    public User updateUserRoleName(@PathVariable Long userId, String roleName){
        User user = userService.findById(userId);
        return userRoleService.updateUserRoleByRoleName(user,roleName);
    }

    @PatchMapping(value="/{userId}/role/update/one")
    public User updateUserRoleName(@PathVariable Long userId, Long roleId){
        User user = userService.findById(userId);
        return userRoleService.updateUserRoleByRoleId(user,roleId);
    }

    @PatchMapping(value="/{userId}/role/add")
    public User addUserRoleByRoleId(@PathVariable Long userId, @RequestBody List<Long> roleIds){
        User user = userService.findById(userId);
        return userRoleService.addUserRoleByRoleId(user,roleIds);
    }

    @PatchMapping(value="/{userId}/role/add/rolename")
    public User addUserRoleByRoleName(@PathVariable Long userId, @RequestBody List<String> roleNames){
        User user = userService.findById(userId);
        return userRoleService.addUserRoleByRoleName(user,roleNames);
    }

    @PatchMapping(value="/{userId}/role/update")
    public User updateUserRoleByRoleId(@PathVariable Long userId, @RequestBody List<Long> roleIds){
        User user = userService.findById(userId);
        return userRoleService.updateUserRoleByRoleId(user,roleIds);
    }

    @PatchMapping(value="/{userId}/role/update/rolename")
    public User updateUserRoleByRoleName(@PathVariable Long userId, @RequestBody List<String> roleNames){
        User user = userService.findById(userId);
        return userRoleService.updateUserRoleByRoleName(user,roleNames);
    }

}
