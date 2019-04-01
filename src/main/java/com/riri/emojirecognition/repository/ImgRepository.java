package com.riri.emojirecognition.repository;

import com.riri.emojirecognition.domain.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sun.util.resources.cldr.gv.LocaleNames_gv;

import java.util.Collection;
import java.util.List;

public interface ImgRepository extends JpaRepository<Img, Long> {

    //获得随机的一组数据，有缺陷，不再使用，情况，id偏前
    @Query(value = "SELECT * FROM img WHERE id >= (ROUND(RAND()*(SELECT MAX(id) FROM img)-(SELECT MIN(id) FROM img)) + (SELECT MIN(id) FROM img)) AND tag = :tag LIMIT :N",nativeQuery=true)
//    @Query(value = "SELECT * FROM Img WHERE tab = :tab ORDER BY RAND() LIMIT :N",nativeQuery=true)
    List<Img>findRandomImgsByTagLimitNum(@Param("tag")String tag,@Param("N")Integer num);

    Img findFirstByTagOrderByIdDesc(String tag);


    @Query(value = "select min(id) from Img")
    Long findMinId();

    @Query(value = "select max(id) from Img")
    Long findMaxId();

    @Query(value = "select min(subId) from Img where tag =:tag")
    Long findMinSubIdByTag(@Param("tag")String tag);
    @Query(value = "select max(subId) from Img where tag =:tag")
    Long findMaxSubIdByTag(@Param("tag")String tag);

    Img findByTagAndSubId(String tag,Long subId);

    Long countByTag(String tag);

    List<Img> findByTagAndSubIdIn(String tag,Collection<?> c);

    List<Img> findByTag(String tag);
}