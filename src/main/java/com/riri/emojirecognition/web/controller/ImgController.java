package com.riri.emojirecognition.web.controller;

import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class ImgController {

    @Autowired
    private ImgService imgService;

    @RequestMapping(value = "json/img/{id}", method = RequestMethod.GET)
    public Img findImg(@PathVariable Long id) {
        return imgService.findById(id);
    }
}
