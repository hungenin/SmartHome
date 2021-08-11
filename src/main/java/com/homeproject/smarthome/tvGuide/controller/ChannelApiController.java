package com.homeproject.smarthome.tvGuide.controller;

import com.homeproject.smarthome.tvGuide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvGuide.model.Channel;
import com.homeproject.smarthome.tvGuide.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import static com.homeproject.smarthome.tvGuide.response.HttpResponse.dataNotFoundByIdResponse;
import static com.homeproject.smarthome.tvGuide.response.HttpResponse.invalidDataResponse;

@RestController
@RequestMapping("/channels")
public class ChannelApiController {
    @Autowired
    private ChannelService channelService;

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Channel channel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        return ResponseEntity.ok(channelService.add(channel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @Valid @RequestBody Channel channel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        try {
            return ResponseEntity.ok(channelService.updateById(id, channel));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Channel", id);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(channelService.findById(id));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Channel", id);
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(channelService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            channelService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Channel", id);
        }
    }
}
