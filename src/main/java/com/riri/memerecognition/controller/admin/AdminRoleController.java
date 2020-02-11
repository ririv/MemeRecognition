package com.riri.memerecognition.controller.admin;

import com.riri.memerecognition.domain.Role;
import com.riri.memerecognition.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/admin/role")
public class AdminRoleController {
    
    private final RoleService roleService;

    @Autowired
    public AdminRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = "operate/{id}")
    public Role get(@PathVariable Long id) {
        return roleService.findById(id);
    }

    @GetMapping(value = "search")
    public Role find(@RequestParam String name) {
        return roleService.findByName(name);
    }

    @GetMapping(value = "query")
    public Page findAll(Pageable pageable) {
        return roleService.findAll(pageable);
    }
}
