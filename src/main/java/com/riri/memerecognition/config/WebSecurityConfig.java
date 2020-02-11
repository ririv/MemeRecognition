package com.riri.memerecognition.config;

import com.riri.memerecognition.security.CustomAuthenticationFailureHandler;
import com.riri.memerecognition.security.CustomAuthenticationSuccessHandler;
import com.riri.memerecognition.security.CustomLoginUrlAuthenticationEntryPoint;
import com.riri.memerecognition.security.CustomLogoutSuccessHandler;
import com.riri.memerecognition.service.impl.UserDetailsServiceImpl;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
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
                //禁用csrf
                .csrf().disable()
                //允许跨域
                .cors()
                .and()
                //页面访问权限需求
                .authorizeRequests()
                    //允许所有用户访问"/"和"/main"
                    .antMatchers("/register","/", "/main","/api/v1/image/**","/**").permitAll()
                    //仅允许拥有“admin”访问此页面
                    .antMatchers("/admin/**","/api//v1/admin/**").hasRole("ADMIN")
                    .antMatchers("/user/**","/api/v1/user/**").hasAnyRole("USER","ADMIN")
                    // 其他地址的访问均需验证权限
                    .anyRequest().authenticated()
                .and()
                //.formLogin() 这个方法不可以少，否则会找不到登陆页面，配置后Spring将生成默认的登陆页面，地址/login，原因请参考父类源码
                .formLogin()
                    //自定义登录页面
                    // 配置此方法后会重定向至自己的登陆页面
                    // 如果没有此页面会返回404找不到页面的信息，一旦配置Spring不会再自动生成登陆页面
                    .loginPage("/login")
                    .permitAll() //允许所有人访问该页面
                    //登录成功返回首页
                    .successHandler(new CustomAuthenticationSuccessHandler()) // 自定义登录成功处理
                    .failureHandler(new CustomAuthenticationFailureHandler()) // 自定义登录失败处理
//                    .defaultSuccessUrl("/")
//                    .failureUrl("/login?error") //登录失败返回页面
                .and()
                .logout()
                    .permitAll()
                    .logoutSuccessUrl("/")
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .and().rememberMe()
                .and().exceptionHandling().authenticationEntryPoint(new CustomLoginUrlAuthenticationEntryPoint());
    }



    //设置加密方式为BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService(){
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
                    .antMatchers("/css/**","/js/**","/image/**","/lib/**","/vue/**");
    }
}