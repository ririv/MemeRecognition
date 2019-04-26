package com.riri.emojirecognition.repository;

import com.riri.emojirecognition.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Long> {

    Model findByEnabled(boolean Enabled);
}
