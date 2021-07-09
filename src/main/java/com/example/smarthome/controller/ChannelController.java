package com.example.smarthome.controller;

import com.example.smarthome.data_sample.ChannelCreator;
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

    @GetMapping("/init")
    public String init() {
        ChannelCreator.getChannels().forEach(channelService::addChannel);

        return "redirect:/channels";
    }

    @GetMapping
    public String channels(Model model) {
        model.addAttribute("channels", channelService.channels());

        return "channels";
    }

    @GetMapping("/{channelId}/follow")
    public String setFollowById(@PathVariable Long channelId) {
        channelService.updateFollowById(Channel.builder().id(channelId).follow(true).build());

        return "redirect:/channels";
    }

    @GetMapping("/{channelId}/unfollow")
    public String setUnfollowById(@PathVariable Long channelId) {
        channelService.updateFollowById(Channel.builder().id(channelId).follow(false).build());

        return "redirect:/channels";
    }



    /*
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
    }*/
}
