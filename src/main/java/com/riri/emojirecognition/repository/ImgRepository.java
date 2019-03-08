package com.riri.emojirecognition.repository;

import com.riri.emojirecognition.domain.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImgRepository extends JpaRepository<Img, Long> {
    @Query(value = "SELECT * FROM img WHERE id >= (ROUND(RAND()*(SELECT MAX(id) FROM img)-(SELECT MIN(id) FROM img)) + (SELECT MIN(id) FROM img)) AND tag = :tag LIMIT :N",nativeQuery=true)
//    @Query(value = "SELECT * FROM Img WHERE tab = :tab ORDER BY RAND() LIMIT :N",nativeQuery=true)
    List<Img>findRandomImgsByTagLimitNum(@Param("tag")String tag,@Param("N")Integer num);
}