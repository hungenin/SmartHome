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

    @RequestMapping("/{programId}")
    public String get(@PathVariable Long programId, Model model) {
        model.addAttribute("program", programService.get(programId));

        return "program";
    }

    @RequestMapping("/update/{programId}")
    public String update(@PathVariable Long programId, @RequestBody Program program) {
        program.setId(programId);
        programService.update(program);

        return "redirect:/programs";
    }

    @RequestMapping("/delete/{programId}")
    public String delete(@PathVariable Long programId) {
        programService.delete(Program.builder().id(programId).build());

        return "redirect:/programs";
    }

    @RequestMapping
    public String programs(Model model) {
        model.addAttribute("programs", programService.programs());

        return "programs";
    }
}
