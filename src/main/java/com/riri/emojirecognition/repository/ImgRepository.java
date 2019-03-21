package com.riri.emojirecognition.repository;

import com.riri.emojirecognition.domain.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sun.util.resources.cldr.gv.LocaleNames_gv;

import java.util.List;

public interface ImgRepository extends JpaRepository<Img, Long> {

    //获得随机的一组数据，有缺陷，不再使用，情况，id偏前
    @Query(value = "SELECT * FROM img WHERE id >= (ROUND(RAND()*(SELECT MAX(id) FROM img)-(SELECT MIN(id) FROM img)) + (SELECT MIN(id) FROM img)) AND tag = :tag LIMIT :N",nativeQuery=true)
//    @Query(value = "SELECT * FROM Img WHERE tab = :tab ORDER BY RAND() LIMIT :N",nativeQuery=true)
    List<Img>findRandomImgsByTagLimitNum(@Param("tag")String tag,@Param("N")Integer num);

    Img findFirstByTagOrderByIdDesc(String tag);


    @Query(value = "SELECT MIN(id) FROM Img")
    Long findMinId();

    @Query(value = "SELECT MAX(id) FROM Img")
    Long findMaxId();

    @Query(value = "SELECT MIN(subId) FROM Img WHERE tag =:tag")
    Long findMinSubIdByTag(@Param("tag")String tag);
    @Query(value = "SELECT MAX(subId) FROM Img WHERE tag =:tag")
    Long findMaxSubIdByTag(@Param("tag")String tag);

    Img findBySubIdAndTag(Long id, String tag);

    Long countByTag(String tag);
}