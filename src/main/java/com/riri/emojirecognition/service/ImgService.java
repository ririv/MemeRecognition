package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.Img;
import com.riri.emojirecognition.repository.ImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImgService {

    @Autowired
    private ImgRepository imgRepository;

    public Img save(Img img) {
        return imgRepository.save(img);
    }
}
