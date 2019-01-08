package com.riri.emojirecognition.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    public class IndexController {
    private static final String INDEX = "index";
        @RequestMapping("/show")
        public String getIndex() {
            return INDEX;
        }
    }
