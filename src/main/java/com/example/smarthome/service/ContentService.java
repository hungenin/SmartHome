package com.example.smarthome.service;

import com.example.smarthome.dao.ContentDao;
import com.example.smarthome.model.tvGuide.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {
    @Autowired
    private ContentDao contentDao;

    public void add(Content content) {
        contentDao.add(content);
    }

    public Content get(Long id) {
        return contentDao.get(id);
    }

    public void update(Long id, Content content) {
        content.setId(id);
        contentDao.update(content);
    }

    public void delete(Long id) {
        contentDao.delete(Content.builder().id(id).build());
    }

    public List<Content> contents() {
        return contentDao.contents();
    }
}
