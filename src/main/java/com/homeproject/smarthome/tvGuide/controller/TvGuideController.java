package com.homeproject.smarthome.tvGuide.controller;

import com.homeproject.smarthome.tvGuide.service.TvGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tv_guide")
public class TvGuideController {
    @Autowired
    private TvGuideService tvGuideService;

    @GetMapping("/init")
    public String init() {
        tvGuideService.init();

        return "redirect:/tv_guide";
    }

    @GetMapping
    public String tvGuide(Model model) {
        model.addAttribute("channels", tvGuideService.followedChannelsWithPrograms());

        return "tv_guide";
    }
}
