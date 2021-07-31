package com.example.smarthome.controller.api;

import com.example.smarthome.model.tvGuide.Program;
import com.example.smarthome.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
public class ProgramApiController {
    @Autowired
    private ProgramService programService;

    @PostMapping
    public void add(@RequestBody Program program) {
        programService.add(program);
    }

    @GetMapping("/{id}")
    public Program get(@PathVariable Long id) {
        return programService.get(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Program program) {
        program.setId(id);
        programService.update(program);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        programService.delete(Program.builder().id(id).build());
    }

    @GetMapping
    public List<Program> programs() {
        return programService.programs();
    }
}
