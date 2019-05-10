package com.riri.emojirecognition.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.riri.emojirecognition.Valid.PasswordMatches;
import com.riri.emojirecognition.Valid.ValidEmail;
import com.riri.emojirecognition.Valid.ValidPassword;

@PasswordMatches
public class UserDto {
    @NotNull
    @Size(min = 1, message = "{Size.userDto.username}")
    private String username;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

    @ValidEmail
    @NotNull
    @Size(min = 1, message = "{Size.userDto.email}")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    private Integer role;

    public Integer getRole() {
        return role;
    }

    public void setRole(final Integer role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(final String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }



    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDto [username=").append(username).append(", password=").append(password).append(", matchingPassword=").append(matchingPassword).append(", email=").append(email)
                .append(", role=").append(role).append("]");
        return builder.toString();
    }

}
