package com.riri.emojirecognition.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;


//用户实体类
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message="用户名不能为空")
    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    //@Email(message="邮箱格式错误")
    @Column(name = "email")
    private String email;

    /**
     * 经测试，使用list时，在增加或修改单个用户权限时，即使有数据一样，数据库也会重新删除，再插入
     * 但使用set时，数据库会查询权限，对比权限是否相同，如果相同则不继续往下操作，少的插入，多的删除
     */
    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Role> roles;


//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        //将用户角色作为权限
//        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
//        Collection<Role> roles = this.getRoles();
//        for(Role role : roles){
//            auths.add(new SimpleGrantedAuthority(role.getName()));
//        }
//        return auths;
//    }


//    @NotNull
//    @Column(name = "enabled")
//    private Boolean enabled;

    public User() {
    }

    public User(@NotNull(message = "用户名不能为空") String username, String password, String email, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

//    @Override
//    public String toString() {
//        return "User [id=" + id + ", username=" + username + ", email=" + email + ", role=" + role + "]";
//    }

}
