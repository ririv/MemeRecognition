package com.riri.emojirecognition.config;

import com.riri.emojirecognition.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    @Autowired
    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //允许跨域
                .cors()
                .and()
                //页面访问权限需求
                .authorizeRequests()
                    //允许所有用户访问"/"和"/home"
                    .antMatchers("/register","/", "/main","/**").permitAll()
                    //仅允许拥有“admin”访问此页面
                    //.antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/user/**").hasRole("USER")
                // 其他地址的访问均需验证权限
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    //自定义登录页面
                    .loginPage("/login")
                   //允许所有人访问该页面
                    .permitAll()
                    //登录成功返回首页
                    .defaultSuccessUrl("/index")
                    //登录失败返回页面
                    //.failureUrl("/login?error")

                .and()
                .logout()
                    .permitAll()
                    .logoutSuccessUrl("/index")
                .and().rememberMe()
                .and().csrf().disable();

    }

    //设置加密方式为BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsServiceImpl userDetailsService(){
        return  new UserDetailsServiceImpl();
    }


    //另一种实现方式
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
//                自定义的验证
//                .authenticationProvider(usernamePasswordAuthenticationProvider());

//                从内存中获取
//                .inMemoryAuthentication()

                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());//配置自定义UserDetails

//                .and()
//                //从数据库中获取
//                .jdbcAuthentication()
//                .dataSource(dataSource)
//
//                //启用密码加密功能
//                .passwordEncoder(passwordEncoder())
//
//                .withDefaultSchema()
//                .withUser("user").password("123456").roles("USER")
//                .and()
//                .withUser("admin").password("123456").roles("USER", "ADMIN");
    }



//    @Bean
//    public UserDetailsService userDetailsService() throws Exception {
//        // ensure the passwords are encoded properly
//        UserBuilder users = User.withDefaultPasswordEncoder();
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(users.username("user").password("password").roles("USER").build());
//        manager.createUser(users.username("admin").password("password").roles("USER","ADMIN").build());
//        return manager;
//    }


//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        // ensure the passwords are encoded properly
//        User.UserBuilder users = User.withDefaultPasswordEncoder();
//        auth
//                .jdbcAuthentication()
//                .dataSource(dataSource)
//                .withDefaultSchema()
//                .withUser(users.username("user").password("password").roles("USER"))
//                .withUser(users.username("admin").password("password").roles("USER","ADMIN"));
//    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }


    @Override
    public void configure(WebSecurity web) {
        //解决静态资源css,js等被拦截的问题
        web
                .ignoring()
                    .antMatchers("/css/**","/js/**","/image/**","/lib/**","/model/**");
    }
}