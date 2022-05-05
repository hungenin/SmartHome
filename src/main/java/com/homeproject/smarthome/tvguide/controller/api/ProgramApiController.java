package com.homeproject.smarthome.tvguide.controller.api;

import com.homeproject.smarthome.tvguide.exception.CannotBeAddedException;
import com.homeproject.smarthome.tvguide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvguide.exception.InvalidDataException;
import com.homeproject.smarthome.tvguide.model.Program;
import com.homeproject.smarthome.tvguide.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static com.homeproject.smarthome.tvguide.response.HttpResponse.*;

// TODO: kivételkezelést átnézni, helyre rakni

@RestController
@RequestMapping("/api/tv_guide/programs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProgramApiController {
    @Autowired
    private ProgramService programService;

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

    @GetMapping
    public ResponseEntity<?> programs() {
        return ResponseEntity.ok(programService.programs());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshPrograms() {
        programService.refreshPrograms();

        return ResponseEntity.ok().build();
    }
}
