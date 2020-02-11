package com.riri.memerecognition.repository;

import com.riri.memerecognition.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Long> {

    Model findByEnabled(boolean Enabled);
}
