package com.homeproject.smarthome.tvGuide.controller;

import com.homeproject.smarthome.tvGuide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvGuide.model.Content;
import com.homeproject.smarthome.tvGuide.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static com.homeproject.smarthome.tvGuide.response.HttpResponse.dataNotFoundByIdResponse;
import static com.homeproject.smarthome.tvGuide.response.HttpResponse.invalidDataResponse;

@RestController
@RequestMapping("/contents")
public class ContentApiController {
    @Autowired
    private ContentService contentService;

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Content content, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        return ResponseEntity.ok(contentService.add(content));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @Valid @RequestBody Content content, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        try {
            return ResponseEntity.ok(contentService.updateById(id, content));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Content", id);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(contentService.findById(id));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Content", id);
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(contentService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            contentService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Content", id);
        }
    }
}
