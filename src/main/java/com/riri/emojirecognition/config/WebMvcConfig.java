package com.riri.emojirecognition.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${web.default-path}")
    private String path;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/login").setViewName("signin.html");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //指向外部目录
        registry.addResourceHandler("/image/**").addResourceLocations("file:"+ path);
    }
}