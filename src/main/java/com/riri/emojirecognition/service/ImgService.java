package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.Img;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.util.*;

public interface ImgService {

     Img save(Img img);

     List<Img> saveAll(List<Img> imgs);

     Img findById(Long id);

     void deleteById(Long id);

     Long findMaxSubIdByTag(String tag);

     Page<Img> findAll(int page, int size);

     Page<Img> findAll(int page, int size, Sort sort);

     Page<Img> findAll(int page, int size, Sort.Direction direction, String... properties);

     Page<Img> findAll(Pageable pageable);

     Page<Img> findByTag(String tag, Pageable pageable);

     Img updateById(Long id, Img img) ;

     Img add(Img img);

     void enableImgById(Long id);

     void enableImg(Img img);

     long generateSubId(String tag);

     void batchInsertToDbByDir(String targetDirPath);

     List<Img> findRandomAndEnabledImgsByTagLimitNum(String tag, int num);

     List<Img> findRandomAndEnabledImgsByTagLimitNum2(String tag, int num);
    
     List<Img> findRandomAndEnabledImgsByTagLimitNum3(String tag, int num);

}

