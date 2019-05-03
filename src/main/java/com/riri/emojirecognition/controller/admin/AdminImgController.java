package com.riri.emojirecognition.controller.admin;

import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/img")
public class AdminImgController {
    private final ImgService imgService;

    @Autowired
    public AdminImgController(ImgService imgService) {
        this.imgService = imgService;
    }

    @GetMapping(value = "operate/{id}")
    public Img get(@PathVariable Long id) {
        return imgService.findById(id);
    }

    @PutMapping(value = "operate/{id}")
    public Img update(@PathVariable Long id, @RequestBody Img img){
        return imgService.updateById(id, img);
    }

    @DeleteMapping(value = "operate/{id}")
    public void delete(@PathVariable Long id) {
        imgService.deleteById(id);
    }

    @PutMapping(value = "enable/{id}")
    public void enableImg(@PathVariable Long id) {
        imgService.enableImgById(id);
    }

    @PostMapping(value = "create")
    public Img create(@RequestBody Img img) {
        return imgService.createImg(img);
    }

    @GetMapping(value = "query")
    public Page findAll(@PageableDefault(sort={"tag"}) Pageable pageable,@RequestParam(required = false) String tag) {
        if (tag == null) {
            return imgService.findAll(pageable);
        }
        else{
            return imgService.findByTag(tag,pageable);
        }
    }

}
