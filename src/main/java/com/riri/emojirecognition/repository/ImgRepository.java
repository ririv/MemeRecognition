package com.riri.emojirecognition.repository;

import com.riri.emojirecognition.domain.Img;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImgRepository extends JpaRepository<Img, Long> {

}
