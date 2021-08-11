package com.homeproject.smarthome.tvGuide.controller.api;

import com.homeproject.smarthome.tvGuide.exception.CannotBeDeletedException;
import com.homeproject.smarthome.tvGuide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvGuide.model.Channel;
import com.homeproject.smarthome.tvGuide.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.homeproject.smarthome.tvGuide.response.HttpResponse.*;

@RestController
@RequestMapping("/api/channels")
public class ChannelApiController {
    @Autowired
    private ChannelService channelService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Channel channel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        return ResponseEntity.ok(channelService.add(channel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(channelService.get(id));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Channel", id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Channel channel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        try {
            return ResponseEntity.ok(channelService.update(id ,channel));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Channel", id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            channelService.delete(id);
            return ResponseEntity.ok().build();
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Channel", id);
        }
    }

    @GetMapping
    public ResponseEntity<?> channels() {
        return ResponseEntity.ok(channelService.channels());
    }

    @GetMapping("/{id}/follow")
    public ResponseEntity<?> setFollow(@PathVariable Long id) {
        try {
            channelService.setFollow(id);
            return ResponseEntity.ok().build();
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Channel", id);
        }
    }

    @GetMapping("/{id}/unfollow")
    public ResponseEntity<?> setUnfollow(@PathVariable Long id) {
        try {
            channelService.setUnFollow(id);
            return ResponseEntity.ok().build();
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Channel", id);
        } catch (CannotBeDeletedException e) {
            return cannotBeDeletedResponse("Channel", id);
        }
    }
}
