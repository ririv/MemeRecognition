package com.riri.emojirecognition.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController {

    private static final String INDEX = "index";

    @RequestMapping("/admin/show")
    public String getIndex() {
        return INDEX;
    }
    @RequestMapping("/user/show")
    public String getUser() {
        return INDEX;
    }


}
