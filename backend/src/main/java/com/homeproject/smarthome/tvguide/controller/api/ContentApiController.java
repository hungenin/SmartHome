package com.homeproject.smarthome.tvguide.controller.api;

import com.homeproject.smarthome.tvguide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvguide.model.Content;
import com.homeproject.smarthome.tvguide.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static com.homeproject.smarthome.tvguide.response.HttpResponse.*;

// TODO: kivételkezelést átnézni, helyre rakni

@RestController
@RequestMapping("/api/tv_guide/contents")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContentApiController {
    @Autowired
    private ContentService contentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(contentService.get(id));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Content", id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Content content, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        try {
            return ResponseEntity.ok(contentService.update(id, content));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Content", id);
        }
    }

    @GetMapping
    public ResponseEntity<?> contents() {
        return ResponseEntity.ok(contentService.contents());
    }
}
