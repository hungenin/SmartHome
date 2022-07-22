package com.homeproject.smarthome.tvguide.controller.api;

import com.homeproject.smarthome.tvguide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvguide.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.homeproject.smarthome.tvguide.response.HttpResponse.dataNotFoundByIdResponse;

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

    @GetMapping
    public ResponseEntity<?> programs() {
        return ResponseEntity.ok(programService.programs());
    }
}
