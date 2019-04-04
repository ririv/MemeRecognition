package com.riri.emojirecognition.controller.background;

import com.riri.emojirecognition.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/img")
public class AdminImgController {
    private final ImgService imgService;

    @Autowired
    public AdminImgController(ImgService imgService) {
        this.imgService = imgService;
    }

    @RequestMapping(value = "a")
    public Page findAll() {
        int a =1;
        int b=10;
        return imgService.findAll(a,b);
    }

    @RequestMapping(value = "b")
    public Page findAll(@PageableDefault(value = 20, sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable) {
        return imgService.findAll(pageable);
    }

    @RequestMapping(value = "c")
    public Page findAll1(Pageable pageable) {
        return imgService.findAll(pageable);
    }

}
