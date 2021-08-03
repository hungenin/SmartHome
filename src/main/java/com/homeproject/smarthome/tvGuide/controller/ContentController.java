package com.homeproject.smarthome.tvGuide.controller;

import com.homeproject.smarthome.tvGuide.model.Content;
import com.homeproject.smarthome.tvGuide.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contents")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @PostMapping
    public String add(@RequestBody Content content) {
        contentService.add(content);

        return "redirect:/programs";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable Long id, Model model) {
        model.addAttribute("content", contentService.get(id));

        return "content";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody Content content) {
        contentService.update(id, content);

        return "redirect:/contents";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        contentService.delete(id);

        return "redirect:/contents";
    }

    @GetMapping
    public String contents(Model model) {
        model.addAttribute("contents", contentService.contents());

        return "contents";
    }
}
