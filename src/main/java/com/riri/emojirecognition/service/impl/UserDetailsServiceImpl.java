package com.riri.emojirecognition.service.impl;

import com.riri.emojirecognition.domain.Role;
import com.riri.emojirecognition.domain.User;
import com.riri.emojirecognition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 登陆验证
     * @param username 用户名
     * @return Spring UserDetails
     * @throws UsernameNotFoundException Spring 用户名未找到异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //取得用户
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("The username not found");
        }

        System.out.println("username:" + user.getUsername() + ";password:" + user.getPassword());

        //返回Spring Security框架提供的User或者自定义的MyUser（implements UserDetails）
        //        return new MyUser(username, userInfo.getPassword(), simpleGrantedAuthorities);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
//                true, true, true, true, //默认为true，可以不写
                authorities(user.getRoles()));

        //另一种写法
//        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        for (Role role : user.getRoles()) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


    // 取得用户的权限

    private Collection<? extends GrantedAuthority> authorities(Collection<Role> roles) {
        //通过角色名获得权限
        List<GrantedAuthority> auths = new ArrayList<>();
        for(Role role : roles){
            auths.add(new SimpleGrantedAuthority(role.getName()));
        }
        return auths;
    }

//    private Collection<? extends GrantedAuthority> authorities(
//            Collection<Role> roles) {
//
//        return getGrantedAuthorities(getAuthorities(roles));
//    }
//
//    private List<String> getAuthorities(Collection<Role> roles) {
//
//        List<String> authorities = new ArrayList<>();
//        List<Authority> collection = new ArrayList<>();
//        for (Role role : roles) {
//            collection.addAll(role.getAuthorities());
//        }
//        for (Authority item : collection) {
//            authorities.add(item.getName());
//        }
//        return authorities;
//    }
//
//    private List<GrantedAuthority> getGrantedAuthorities(List<String> authorities) {
//        List<GrantedAuthority> collection= new ArrayList<>();
//        for (String privilege : authorities) {
//            collection.add(new SimpleGrantedAuthority(privilege));
//        }
//        return collection;
//    }

}
