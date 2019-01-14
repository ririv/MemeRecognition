package com.riri.emojirecognition.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/")
    public class IndexController {
    private static final String INDEX = "index";
        @RequestMapping("/show")
        public String getIndex() {
            return INDEX;
        }

        @RequestMapping("/signin")
        public String signin() {
            return INDEX;
        }
    }
