package com.homeproject.smarthome.tvGuide.dao.implementation.memory;

import com.homeproject.smarthome.tvGuide.dao.ContentDao;
import com.homeproject.smarthome.tvGuide.model.Content;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ContentDaoMemory implements ContentDao {
    private final AtomicLong idCounter = new AtomicLong(1);
    private final List<Content> contents = new ArrayList<>();

    @Override
    public Content add(Content content) {
        if (content.getId() == null || !contents.contains(content)){
            content.setId(idCounter.getAndIncrement());
            contents.add(content.toBuilder().build());
        }

        return content;
    }

    @Override
    public Optional<Content> get(Long id) {
        return contents.stream()
                .filter(content -> content.getId().equals(id))
                .findFirst();
    }

    @Override
    public Content update(Content content) {
        contents.stream()
                .filter(content::equals)
                .forEach(content1 -> {
                    content1.setTitle(content.getTitle());
                    content1.setDescription(content.getDescription());
                    content1.setPrograms(content.getPrograms());
                });

        return content;
    }

    @Override
    public void delete(Content content) {
        contents.removeIf(content1 -> content1.equals(content) && (content1.getPrograms() == null || content1.getPrograms().isEmpty()));
    }

    @Override
    public List<Content> contents() {
        return new ArrayList<>(contents);
    }

    @Override
    public boolean existsById(Long id) {
        for (Content content : contents) {
            if (content.getId().equals(id)) return true;
        }
        return false;
    }
}
