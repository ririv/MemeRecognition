package com.riri.emojirecognition.service;

import com.riri.emojirecognition.domain.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ModelService {
     Model save(Model model);

     Model findById(Long id);

     Model findByEnabled(boolean enabled);

     void deleteById(Long id);

     Model updateById(Long id, Model model);

     Model createModel(Model model);

     Page<Model> findAll(Pageable pageable);
}
