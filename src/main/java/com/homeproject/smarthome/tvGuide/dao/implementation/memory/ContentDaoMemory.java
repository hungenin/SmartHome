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
    public Content save(Content content) {
        if (content.getId() == null || !contents.contains(content)){
            content.setId(idCounter.getAndIncrement());
            contents.add(content.toBuilder().build());
        } else {
            contents.stream()
                    .filter(content::equals)
                    .forEach(content1 -> {
                        content1.setTitle(content.getTitle());
                        content1.setDescription(content.getDescription());
                        content1.setPrograms(content.getPrograms());
                    });
        }

        return content;
    }

    @Override
    public Optional<Content> findById(Long id) {
        return contents.stream()
                .filter(content -> content.getId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        contents.removeIf(content -> content.getId().equals(id) && (content.getPrograms() == null || content.getPrograms().isEmpty()));
    }

    @Override
    public List<Content> findAll() {
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
