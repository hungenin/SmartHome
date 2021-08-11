package com.homeproject.smarthome.tvGuide.dao;

import com.homeproject.smarthome.tvGuide.model.Content;
import java.util.List;
import java.util.Optional;

public interface ContentDao {
    Content add(Content content);
    Optional<Content> get(Long id);
    Content update(Content content);
    void delete(Content content);
    List<Content> contents();
    boolean existsById(Long id);
}
