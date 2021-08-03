package com.homeproject.smarthome.tvGuide.controller.api;

import com.homeproject.smarthome.tvGuide.model.Channel;
import com.homeproject.smarthome.tvGuide.model.dto.ChannelDto;
import com.homeproject.smarthome.tvGuide.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/channels")
public class ChannelApiController {
    @Autowired
    private ChannelService channelService;

    @PostMapping
    public void add(@RequestBody Channel channel) {
        channelService.add(channel);
    }

    @GetMapping("/{id}")
    public ChannelDto get(@PathVariable Long id) {
        return channelService.get(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Channel channel) {
        channelService.update(id ,channel);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        channelService.delete(id);
    }

    @GetMapping
    public List<ChannelDto> channels() {
        return channelService.channels();
    }

    @GetMapping("/{id}/follow")
    public void setFollow(@PathVariable Long id) {
        channelService.setFollow(id);
    }

    @GetMapping("/{id}/unfollow")
    public void setUnfollow(@PathVariable Long id) {
        channelService.setUnFollow(id);
    }
}
