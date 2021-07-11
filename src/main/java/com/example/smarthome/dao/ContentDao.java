package com.example.smarthome.dao;

import com.example.smarthome.model.tvGuide.Content;

import java.util.List;

public interface ContentDao {
    void add(Content content);
    Content get(Long id);
    void update(Content content);
    void delete(Content content);
    List<Content> contents();
}
