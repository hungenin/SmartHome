package com.homeproject.smarthome.tvGuide.controller;

import com.homeproject.smarthome.tvGuide.model.Channel;
import com.homeproject.smarthome.tvGuide.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/channels")
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
        channelService.update(id, channel);

        return "redirect:/channels";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        channelService.delete(id);

        return "redirect:/channels";
    }

    @GetMapping
    public String channels(Model model) {
        model.addAttribute("channels", channelService.channels());

        return "channels";
    }

    @GetMapping("/{id}/follow")
    public String setFollow(@PathVariable Long id) {
        channelService.setFollow(id);

        return "redirect:/channels";
    }

    @GetMapping("/{id}/unfollow")
    public String setUnfollow(@PathVariable Long id) {
        channelService.setUnFollow(id);

        return "redirect:/channels";
    }
}
