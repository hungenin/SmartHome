package com.example.smarthome.controller.rest;

import com.example.smarthome.model.tvGuide.Content;
import com.example.smarthome.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rest/contents")
public class ContentRestController {
    @Autowired
    private ContentService contentService;

    @PostMapping
    public void add(@RequestBody Content content) {
        contentService.add(content);
    }

    @GetMapping("/{id}")
    public Content get(@PathVariable Long id) {
        return contentService.get(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Content content) {
        content.setId(id);
        contentService.update(content);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        contentService.delete(Content.builder().id(id).build());
    }

    @GetMapping
    public List<Content> contents() {
        return contentService.contents();
    }
}
