package com.homeproject.smarthome.tvguide.controller.api;

import com.homeproject.smarthome.tvguide.exception.CannotBeReadException;
import com.homeproject.smarthome.tvguide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvguide.model.Channel;
import com.homeproject.smarthome.tvguide.model.dto.ChannelDto;
import com.homeproject.smarthome.tvguide.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import static com.homeproject.smarthome.tvguide.response.HttpResponse.*;

@RestController
@RequestMapping("/api/tv_guide/channels")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChannelApiController {
    @Autowired
    private ChannelService channelService;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Short id) {
        try {
            return ResponseEntity.ok(channelService.get(id));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Channel", id);
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Channel channel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return invalidDataResponse(bindingResult);
        }
        try {
            return ResponseEntity.ok(channelService.update(channel));
        } catch (DataNotFoundException e) {
            return dataNotFoundByIdResponse("Channel", (short) 1);
        }
    }

    @GetMapping
    public List<ChannelDto> channels() {
        try {
            return channelService.channelsWithoutPrograms();
        } catch (CannotBeReadException e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/followed")
    public ResponseEntity<?> followedChannelsWithPrograms() {
        return ResponseEntity.ok(channelService.followedChannelsWithPrograms());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateChannelList() {
        channelService.updateChannelList();

        return ResponseEntity.ok().build();
    }

    @PutMapping("/follow_all")
    public ResponseEntity<?> setAllFollow() {
        channelService.setAllFollow();

        return ResponseEntity.ok().build();
    }

    @PutMapping("/unfollow_all")
    public ResponseEntity<?> setAllUnFollow() {
        channelService.setAllUnFollow();

        return ResponseEntity.ok().build();
    }
}


