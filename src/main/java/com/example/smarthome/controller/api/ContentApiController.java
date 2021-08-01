package com.example.smarthome.controller.api;

import com.example.smarthome.model.tvGuide.Content;
import com.example.smarthome.model.tvGuide.dto.ContentDto;
import com.example.smarthome.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contents")
public class ContentApiController {
    @Autowired
    private ContentService contentService;

    @PostMapping
    public void add(@RequestBody Content content) {
        contentService.add(content);
    }

    @GetMapping("/{id}")
    public ContentDto get(@PathVariable Long id) {
        return contentService.get(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Content content) {
        contentService.update(id, content);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        contentService.delete(id);
    }

    @GetMapping
    public List<ContentDto> contents() {
        return contentService.contents();
    }
}
