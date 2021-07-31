package com.example.smarthome.controller.api;

import com.example.smarthome.model.tvGuide.Channel;
import com.example.smarthome.service.ChannelService;
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
    public Channel get(@PathVariable Long id) {
        return channelService.get(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Channel channel) {
        channel.setId(id);
        channelService.update(channel);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        channelService.delete(Channel.builder().id(id).build());
    }

    @GetMapping
    public List<Channel> channels() {
        return channelService.channels();
    }

    @GetMapping("/{channelId}/follow")
    public void setFollow(@PathVariable Long channelId) {
        channelService.setFollow(Channel.builder().id(channelId).follow(true).build());
    }

    @GetMapping("/{channelId}/unfollow")
    public void setUnfollow(@PathVariable Long channelId) {
        channelService.setFollow(Channel.builder().id(channelId).follow(false).build());
    }
}
