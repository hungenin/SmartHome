package com.example.smarthome.controller.api;

import com.example.smarthome.model.tvGuide.Program;
import com.example.smarthome.model.tvGuide.dto.ProgramDto;
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
    public ProgramDto get(@PathVariable Long id) {
        return programService.get(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Program program) {
        programService.update(id, program);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        programService.delete(id);
    }

    @GetMapping
    public List<ProgramDto> programs() {
        return programService.programs();
    }
}
