package com.example.smarthome.controller;

import com.example.smarthome.model.tvGuide.Program;
import com.example.smarthome.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/programs")
public class ProgramController {
    @Autowired
    private ProgramService programService;

    @RequestMapping("/add")
    public String add(@RequestBody Program program) {
        programService.add(program);

        return "redirect:/programs";
    }

    @RequestMapping("/{id}")
    public String get(@PathVariable Long id, Model model) {
        model.addAttribute("program", programService.get(id));

        return "program";
    }

    @RequestMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestBody Program program) {
        program.setId(id);
        programService.update(program);

        return "redirect:/programs";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        programService.delete(Program.builder().id(id).build());

        return "redirect:/programs";
    }

    @RequestMapping
    public String programs(Model model) {
        model.addAttribute("programs", programService.programs());

        return "programs";
    }
}
