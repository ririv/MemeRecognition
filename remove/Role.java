package com.riri.emojirecognition.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "role")
public class Role implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;



    private String name;
        @ManyToMany(mappedBy = "roles")
        private Collection<User> users;



    @ManyToMany
        @JoinTable(
                name = "roles_authorities",
                joinColumns = @JoinColumn(
                        name = "role_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(
                        name = "authority_id", referencedColumnName = "id"))
        private Collection<Authority> authorities;

    public Collection<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<Authority> authorities) {
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    }