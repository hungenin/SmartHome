package com.homeproject.smarthome.tvGuide.dao;

import com.homeproject.smarthome.tvGuide.model.Content;
import java.util.List;
import java.util.Optional;

public interface ContentDao {
    Content save(Content content);
    Optional<Content> findById(Long id);
    void deleteById(Long id);
    List<Content> findAll();
    boolean existsById(Long id);
}
