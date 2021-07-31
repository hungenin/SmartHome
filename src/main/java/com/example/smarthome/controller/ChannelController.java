package com.example.smarthome.controller;

import com.example.smarthome.model.tvGuide.Channel;
import com.example.smarthome.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("channels")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @PostMapping
    public String add(@RequestBody Channel channel) {
        channelService.add(channel);

        return "redirect:/channels";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable Long id, Model model) {
        model.addAttribute("program", channelService.get(id));

        return "channel";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @RequestBody Channel channel) {
        channel.setId(id);
        channelService.update(channel);

        return "redirect:/channels";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        channelService.delete(Channel.builder().id(id).build());

        return "redirect:/channels";
    }

    @GetMapping
    public String channels(Model model) {
        model.addAttribute("channels", channelService.channels());

        return "channels";
    }

    @GetMapping("/{channelId}/follow")
    public String setFollow(@PathVariable Long channelId) {
        channelService.setFollow(Channel.builder().id(channelId).follow(true).build());

        return "redirect:/channels";
    }

    @GetMapping("/{channelId}/unfollow")
    public String setUnfollow(@PathVariable Long channelId) {
        channelService.setFollow(Channel.builder().id(channelId).follow(false).build());

        return "redirect:/channels";
    }
}
