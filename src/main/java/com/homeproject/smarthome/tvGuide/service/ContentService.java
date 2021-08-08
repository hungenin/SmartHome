package com.homeproject.smarthome.tvGuide.service;

import com.homeproject.smarthome.tvGuide.dao.ContentDao;
import com.homeproject.smarthome.tvGuide.model.Content;
import com.homeproject.smarthome.tvGuide.model.dto.ContentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentService {
    @Autowired
    private ContentDao contentDao;

    public ContentDto add(Content content) {
        content.setId(null);
        return new ContentDto(contentDao.add(content));
    }

    public ContentDto get(Long id) {
        return new ContentDto(contentDao.get(id));
    }

    public ContentDto update(Long id, Content content) {
        content.setId(id);
        return new ContentDto(contentDao.update(content));
    }

    public void delete(Long id) {
        contentDao.delete(Content.builder().id(id).build());
    }

    public List<ContentDto> contents() {
        return contentDao.contents().stream().map(ContentDto::new).collect(Collectors.toList());
    }
}
