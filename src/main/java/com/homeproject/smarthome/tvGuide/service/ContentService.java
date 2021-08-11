package com.homeproject.smarthome.tvGuide.service;

import com.homeproject.smarthome.tvGuide.dao.ContentDao;
import com.homeproject.smarthome.tvGuide.exception.DataNotFoundException;
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
        return new ContentDto(contentDao.get(id).orElseThrow(DataNotFoundException::new));
    }

    public ContentDto update(Long id, Content content) {
        if (contentDao.existsById(id)) {
            content.setId(id);
            return new ContentDto(contentDao.update(content));
        } else {
            throw new DataNotFoundException();
        }
    }

    public void delete(Long id) {
        if (contentDao.existsById(id)) {
            contentDao.delete(Content.builder().id(id).build());
        } else {
            throw new DataNotFoundException();
        }
    }

    public List<ContentDto> contents() {
        return contentDao.contents().stream().map(ContentDto::new).collect(Collectors.toList());
    }
}
