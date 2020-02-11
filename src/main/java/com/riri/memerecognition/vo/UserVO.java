package com.riri.memerecognition.vo;

import com.riri.memerecognition.domain.Role;
import com.riri.memerecognition.domain.User;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserVO {

    private Long id;

    private String username;

    private String password;

    private String email;

    private String roleNames;

    public UserVO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        List<String> roleNamesList = new ArrayList<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                roleNamesList.add(role.getName().replace("ROLE_", "").toLowerCase());
            }
            this.roleNames = StringUtils.join(roleNamesList, ",");
        }
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRoleNames() {
        return roleNames;
    }
}
