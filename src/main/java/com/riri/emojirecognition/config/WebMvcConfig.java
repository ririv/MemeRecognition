package com.riri.emojirecognition.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${web.default-path}")
    private String defaultPath;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/login").setViewName("signin.html");

    }

    //将网址映射本地目录
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**").addResourceLocations("file:"+ defaultPath);
    }

    //Cors跨域请求
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry
//                .addMapping("/**")
                .addMapping("/api/**")
                .allowedMethods("*")
                //允许所有的请求域名访问我们的跨域资源，也可以设置单条或者多条内容
//                .allowedOrigins("*")
                .allowedOrigins("*")
                .allowCredentials (true)
                .allowedHeaders("*")
                //准备响应前的缓存持续的最大时间（以秒为单位）
                .maxAge(3600);
    }
}