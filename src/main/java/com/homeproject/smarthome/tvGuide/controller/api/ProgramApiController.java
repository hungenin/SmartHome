package com.homeproject.smarthome.tvGuide.controller.api;

import com.homeproject.smarthome.tvGuide.model.Program;
import com.homeproject.smarthome.tvGuide.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.homeproject.smarthome.tvGuide.response.HttpResponse.invalidDataResponse;

@RestController
@RequestMapping("/api/programs")
public class ProgramApiController {
    @Autowired
    private ProgramService programService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Program program, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        return ResponseEntity.ok(programService.add(program));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(programService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Program program, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        return ResponseEntity.ok(programService.update(id, program));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        programService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> programs() {
        return ResponseEntity.ok(programService.programs());
    }
}
