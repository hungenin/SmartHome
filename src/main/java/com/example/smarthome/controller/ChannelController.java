package com.example.smarthome.controller;

import com.example.smarthome.data_sample.ChannelCreator;
import com.example.smarthome.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("channels")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @GetMapping("/init")
    public String init(){
        ChannelCreator.getChannels().forEach(channelService::addChannel);

        return "redirect:/";
    }

    @GetMapping
    public String channels(Model model) {
        model.addAttribute("channels", channelService.channels());

        return "channels";
    }

    @GetMapping("/refresh/channel_list")
    public String refreshChannelsList() {
        return "";
    }

    @GetMapping("/refresh")
    public String refreshFollowedChannels() {
        return "";
    }

    @GetMapping("/refresh/{channelId}")
    public String refreshChannelById(@PathVariable long channelId) {
        return "";
    }
}
