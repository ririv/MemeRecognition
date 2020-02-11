package com.riri.memerecognition.controller.admin;

import com.riri.memerecognition.domain.User;
import com.riri.memerecognition.service.UserRoleService;
import com.riri.memerecognition.service.UserService;
import com.riri.memerecognition.vo.UserVO;
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
    public UserVO addUserVORoleByRoleName(@PathVariable Long userId, String roleName){
        User user = userService.findById(userId);
        return new UserVO(userRoleService.addUserRoleByRoleName(user,roleName));
    }

    @PatchMapping(value="/{userId}/role/add/one/")
    public UserVO addUserVORoleByRoleName(@PathVariable Long userId, Long roleId){
        User user = userService.findById(userId);
        return new UserVO(userRoleService.addUserRoleByRoleId(user,roleId));
    }

    @PatchMapping(value="/{userId}/role/update/one/rolename")
    public UserVO updateUserVORoleName(@PathVariable Long userId, String roleName){
        User user = userService.findById(userId);
        return new UserVO(userRoleService.updateUserRoleByRoleName(user,roleName));
    }

    @PatchMapping(value="/{userId}/role/update/one")
    public UserVO updateUserVORoleName(@PathVariable Long userId, Long roleId){
        User user = userService.findById(userId);
        return new UserVO(userRoleService.updateUserRoleByRoleId(user,roleId));
    }

    @PatchMapping(value="/{userId}/role/add")
    public UserVO addUserVORoleByRoleId(@PathVariable Long userId, @RequestBody List<Long> roleIds){
        User user = userService.findById(userId);
        return new UserVO(userRoleService.addUserRoleByRoleId(user,roleIds));
    }

    @PatchMapping(value="/{userId}/role/add/rolename")
    public UserVO addUserVORoleByRoleName(@PathVariable Long userId, @RequestBody List<String> roleNames){
        User user = userService.findById(userId);
        return new UserVO(userRoleService.addUserRoleByRoleName(user,roleNames));
    }

    @PatchMapping(value="/{userId}/role/update")
    public UserVO updateUserVORoleByRoleId(@PathVariable Long userId, @RequestBody List<Long> roleIds){
        User user = userService.findById(userId);
        return new UserVO(userRoleService.updateUserRoleByRoleId(user,roleIds));
    }

    @PatchMapping(value="/{userId}/role/update/rolename")
    public UserVO updateUserVORoleByRoleName(@PathVariable Long userId, @RequestBody List<String> roleNames){
        User user = userService.findById(userId);
        return new UserVO(userRoleService.updateUserRoleByRoleName(user,roleNames));
    }

}
