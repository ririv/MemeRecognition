package com.riri.emojirecognition.controller.background;

import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
    public Img getImg(@PathVariable Long id) {
        return imgService.findById(id);
    }

    @PutMapping(value = "operate/{id}")
    public Img updateImg(@PathVariable Long id, @RequestBody Img img) {
        return imgService.updateImgById(id, img);
    }

    @DeleteMapping(value = "operate/{id}")
    public void deleteImg(@PathVariable Long id) {
        imgService.deleteById(id);
    }

    @PostMapping(value = "create")
    public Img createUser(@RequestBody Img img) {
        return imgService.createImg(img);
    }

    @GetMapping(value = "query")
    public Page findAll(@PageableDefault(value = 20, sort={"tag"}) Pageable pageable,@RequestParam(required = false) String tag) {
        if (tag == null) {
            return imgService.findAll(pageable);
        }
        else{
            return imgService.findByTag(tag,pageable);
        }
    }

}
