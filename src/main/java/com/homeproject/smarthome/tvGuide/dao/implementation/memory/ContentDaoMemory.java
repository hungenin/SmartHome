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
    private final Map<Long, Set<Long>> contentIdToProgramIds = new HashMap<>();

    @Override
    public Content add(Content content) {
        if (content.getId() == null || !contents.contains(content)){
            content.setId(idCounter.getAndIncrement());
            contents.add(content.toBuilder().build());
        }

        return content;
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
        contents.removeIf(content1 -> content1.equals(content) && (content1.getPrograms() == null || content1.getPrograms().size() == 0));
    }

    @Override
    public List<Content> contents() {
        return new ArrayList<>(contents);
    }

    @Override
    public void addContentToProgram(Long contentId, Long programId) {
        if (!contentIdToProgramIds.containsKey(contentId)){
            contentIdToProgramIds.put(contentId, new HashSet<>());
        }
        contentIdToProgramIds.get(contentId).add(programId);
    }

    @Override
    public Content contentByProgram(Long id) {
        return contentIdToProgramIds.entrySet()
                .stream()
                .filter(longSetEntry -> longSetEntry.getValue().contains(id))
                .map(Map.Entry::getKey)
                .map(this::get)
                .findFirst()
                .orElse(null);
    }
}
