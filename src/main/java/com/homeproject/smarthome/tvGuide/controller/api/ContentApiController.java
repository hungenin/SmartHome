package com.homeproject.smarthome.tvGuide.controller.api;

import com.homeproject.smarthome.tvGuide.model.Content;
import com.homeproject.smarthome.tvGuide.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static com.homeproject.smarthome.tvGuide.response.HttpResponse.invalidDataResponse;

@RestController
@RequestMapping("/api/contents")
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

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Content content, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        return ResponseEntity.ok(contentService.update(id, content));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        contentService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> contents() {
        return ResponseEntity.ok(contentService.contents());
    }
}
