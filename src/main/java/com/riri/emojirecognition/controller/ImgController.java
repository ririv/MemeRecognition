package com.riri.emojirecognition.controller;

import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/img")
public class ImgController {

    private final ImgService imgService;

    @Autowired
    public ImgController(ImgService imgService) {
        this.imgService = imgService;
    }

    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public Img findImg(@PathVariable Long id) {
        return imgService.findById(id);
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public List<Img> findImg(@RequestParam("tag") String tag) {
        return imgService.findRandomImgsByTagLimitNum(tag,5);
    }
}
