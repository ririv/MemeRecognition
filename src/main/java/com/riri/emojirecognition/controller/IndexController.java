package com.riri.emojirecognition.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
//此类仅用来做页面映射，也可以在MvcConfig中配置
//此类将删除，请在MvcConfig中做页面映射
public class IndexController {
//    @GetMapping("/main")
//    public ModelAndView main() {
//        return new ModelAndView("main.html");
//    }
//
//    @GetMapping("/login")
//    public ModelAndView login() {
//        return new ModelAndView("signin.html");
//    }
//
//    @GetMapping("/index")
//    public ModelAndView index() {
//        return new ModelAndView("index");
//    }
//
//    @GetMapping("/register")
//    public ModelAndView register() {
//        return new ModelAndView("signup.html");
//    }
}
