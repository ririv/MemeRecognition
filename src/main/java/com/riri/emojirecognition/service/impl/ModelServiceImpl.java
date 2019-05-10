package com.riri.emojirecognition.service.impl;

import com.riri.emojirecognition.domain.Model;
import com.riri.emojirecognition.exception.notfound.ModelNotFoundException;
import com.riri.emojirecognition.repository.ModelRepository;
import com.riri.emojirecognition.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;

    @Autowired
    public ModelServiceImpl(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public Model save(Model model){
        return modelRepository.save(model);
    }

    public Model findById(Long id) {
        Optional<Model> model = modelRepository.findById(id);
        if (!model.isPresent()) {
            throw new ModelNotFoundException("The model is not found, " + "model id: " + id);
        }
        return model.get();
    }

    public Model findByEnabled(boolean enabled) {
        return modelRepository.findByEnabled(enabled);
    }

    public void deleteById(Long id) {
        modelRepository.deleteById(id);
    }

    public Model updateById(Long id, Model model) {
        findById(id);//如果id不存在则会报出异常
        //设置id为所指定的id，防止user中有id信息，而发生更新错位的现象
        model.setId(id);
        return modelRepository.save(model);
    }

    public Model addModel(Model model) {
        model.setId(null);
        return modelRepository.save(model);
    }

    public Page<Model> findAll(Pageable pageable) {
        return modelRepository.findAll(pageable);
    }

    public List<Model> findAll(){return modelRepository.findAll();}
}
