package com.example.smarthome.controller;

import com.example.smarthome.model.tvGuide.Program;
import com.example.smarthome.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/programs")
public class ProgramController {
    @Autowired
    private ProgramService programService;

    @PostMapping
    public String add(@RequestBody Program program) {
        programService.add(program);

        return "redirect:/programs";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable Long id, Model model) {
        model.addAttribute("program", programService.get(id));

        return "program";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody Program program) {
        programService.update(id, program);

        return "redirect:/programs";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        programService.delete(id);

        return "redirect:/programs";
    }

    @GetMapping
    public String programs(Model model) {
        model.addAttribute("programs", programService.programs());

        return "programs";
    }
}
