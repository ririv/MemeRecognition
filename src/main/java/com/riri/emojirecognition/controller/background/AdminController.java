package com.riri.emojirecognition.controller.background;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
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


}
