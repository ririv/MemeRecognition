package com.riri.emojirecognition.vo;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.domain.User;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserVO {

    private Long id;

    private String username;

    private String email;

    private String roleNames;

    public UserVO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        List<String> roleNamesList = new ArrayList<>();
        for (Role role: user.getRoles()){
            roleNamesList.add(role.getName().replace("ROLE_","").toLowerCase());
        }
        this.roleNames = StringUtils.join(roleNamesList,",");
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRoleNames() {
        return roleNames;
    }
}
