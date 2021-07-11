package com.example.smarthome.dao.implementation.memory;

import com.example.smarthome.dao.ContentDao;
import com.example.smarthome.model.tvGuide.Content;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ContentDaoMemory implements ContentDao {
    private AtomicLong idCounter = new AtomicLong(1);
    private List<Content> contents = new ArrayList<>();

    @Override
    public void add(Content content) {
        if (content.getId() == null || !contents.contains(content)){
            content.setId(idCounter.getAndIncrement());
            contents.add(content.toBuilder().build());
        }
    }

    @Override
    public Content get(Long id) {
        return contents.stream()
                .filter(content -> content.getId().equals(id))
                .map(content -> content.toBuilder().build())
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Content content) {
        contents.stream()
                .filter(content::equals)
                .forEach(content1 -> {
                    content1.setTitle(content.getTitle());
                    content1.setDescription(content.getDescription());
                });
    }

    @Override
    public void delete(Content content) {
        contents.removeIf(content::equals);
    }

    @Override
    public List<Content> contents() {
        return new ArrayList<>(contents);
    }
}
