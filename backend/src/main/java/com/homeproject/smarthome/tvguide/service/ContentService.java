package com.homeproject.smarthome.tvguide.service;

import com.homeproject.smarthome.tvguide.dao.ContentDao;
import com.homeproject.smarthome.tvguide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvguide.model.dto.ContentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentService {
    @Autowired
    private ContentDao contentDao;

    public ContentDto get(Long id) {
        return new ContentDto(contentDao.findById(id).orElseThrow(DataNotFoundException::new));
    }

    public List<ContentDto> contents() {
        return contentDao.findAll().stream().map(ContentDto::new).collect(Collectors.toList());
    }
}
