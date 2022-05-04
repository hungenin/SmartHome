package com.homeproject.smarthome.tvGuide.controller.api;

import com.homeproject.smarthome.tvGuide.exception.CannotBeAddedException;
import com.homeproject.smarthome.tvGuide.exception.CannotBeDeletedException;
import com.homeproject.smarthome.tvGuide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvGuide.exception.InvalidDataException;
import com.homeproject.smarthome.tvGuide.model.Program;
import com.homeproject.smarthome.tvGuide.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.homeproject.smarthome.tvGuide.response.HttpResponse.*;

@RestController
@RequestMapping("/api/programs")
public class ProgramApiController {
    @Autowired
    private ProgramService programService;

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Program program, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        try {
            return ResponseEntity.ok(programService.add(program));
        } catch (InvalidDataException e) {
            return invalidDataResponse(e.getMessage());
        } catch (CannotBeAddedException e) {
            return dataNotFoundByIdResponse(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(programService.get(id));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Program", id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Program program, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        try {
            return ResponseEntity.ok(programService.update(id, program));
        } catch (InvalidDataException e) {
            return invalidDataResponse(e.getMessage());
        } catch (CannotBeAddedException e) {
            return dataNotFoundByIdResponse(e.getMessage());
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Program", id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            programService.delete(id);
            return ResponseEntity.ok().build();
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Program", id);
        } catch (CannotBeDeletedException e) {
            return cannotBeDeletedResponse("Program", id);
        }
    }

    @GetMapping
    public ResponseEntity<?> programs() {
        return ResponseEntity.ok(programService.programs());
    }
}
