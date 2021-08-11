package com.homeproject.smarthome.tvGuide.dao;

import com.homeproject.smarthome.tvGuide.model.Content;

import java.util.List;

public interface ContentDao {
    Content add(Content content);
    Content get(Long id);
    Content update(Content content);
    void delete(Content content);
    List<Content> contents();
}
