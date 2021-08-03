package com.homeproject.smarthome.tvGuide.dao;

import com.homeproject.smarthome.tvGuide.model.Content;

import java.util.List;

public interface ContentDao {
    void add(Content content);
    Content get(Long id);
    void update(Content content);
    void delete(Content content);
    List<Content> contents();

    void addContentToProgram(Long contentId, Long programId);
    Content contentByProgram(Long id);
}
