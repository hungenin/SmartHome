package com.example.smarthome.controller;

import com.example.smarthome.data_sample.ChannelCreator;
import com.example.smarthome.data_sample.ContentCreator;
import com.example.smarthome.data_sample.ProgramCreator;
import com.example.smarthome.service.ChannelService;
import com.example.smarthome.service.ContentService;
import com.example.smarthome.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("tv_guide")
public class TvGuideController {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ProgramService programService;
    @Autowired
    private ContentService contentService;

    @GetMapping("/init")
    public String init() {
        ChannelCreator.getChannels().forEach(channelService::add);
        ProgramCreator.getPrograms().forEach(programService::add);
        ContentCreator.getContents().forEach(contentService::add);

        return "redirect:/tv_guide";
    }
}
