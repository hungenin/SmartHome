package com.example.smarthome.controller;

import com.example.smarthome.model.tvGuide.Content;
import com.example.smarthome.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contents")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping("/add")
    public String add(@RequestBody Content content) {
        contentService.add(content);

        return "redirect:/programs";
    }

    @RequestMapping("/{id}")
    public String get(@PathVariable Long id, Model model) {
        model.addAttribute("content", contentService.get(id));

        return "content";
    }

    @RequestMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestBody Content content) {
        content.setId(id);
        contentService.update(content);

        return "redirect:/contents";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        contentService.delete(Content.builder().id(id).build());

        return "redirect:/contents";
    }

    @RequestMapping
    public String contents(Model model) {
        model.addAttribute("contents", contentService.contents());

        return "contents";
    }
}
