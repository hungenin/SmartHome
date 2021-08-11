package com.homeproject.smarthome.tvGuide.service;

import com.homeproject.smarthome.tvGuide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvGuide.dao.ContentRepository;
import com.homeproject.smarthome.tvGuide.model.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;

    public Content add(Content content) {
        content.setId(null);
        return contentRepository.save(content);
    }
    
    public Content updateById(Long id, Content content) {
        if (contentRepository.existsById(id)) {
            content.setId(id);
            return contentRepository.save(content);
        } else {
            throw new DataNotFoundException();
        }
    }

    public Content findById(Long id) {
        return contentRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }

    public List<Content> findAll() {
        List<Content> list = new ArrayList<>();
        contentRepository.findAll().forEach(list::add);
        return list;
    }

    public void deleteById(Long id) {
        if (contentRepository.existsById(id)) {
            contentRepository.deleteById(id);
        } else {
            throw new DataNotFoundException();
        }
    }
}
