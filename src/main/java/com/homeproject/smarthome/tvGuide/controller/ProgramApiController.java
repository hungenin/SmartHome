package com.homeproject.smarthome.tvGuide.controller;

import com.homeproject.smarthome.tvGuide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvGuide.model.Program;
import com.homeproject.smarthome.tvGuide.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static com.homeproject.smarthome.tvGuide.response.HttpResponse.dataNotFoundByIdResponse;
import static com.homeproject.smarthome.tvGuide.response.HttpResponse.invalidDataResponse;

@RestController
@RequestMapping("/programs")
public class ProgramApiController {
    @Autowired
    private ProgramService programService;

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Program program, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        return ResponseEntity.ok(programService.add(program));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @Valid @RequestBody Program program, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        try {
            return ResponseEntity.ok(programService.updateById(id, program));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Program", id);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(programService.findById(id));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Program", id);
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(programService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            programService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Program", id);
        }
    }
}
