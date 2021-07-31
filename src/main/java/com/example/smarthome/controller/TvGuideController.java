package com.example.smarthome.controller;

import com.example.smarthome.data_sample.ChannelCreator;
import com.example.smarthome.data_sample.ContentCreator;
import com.example.smarthome.data_sample.ProgramCreator;
import com.example.smarthome.model.tvGuide.dto.ChannelDto;
import com.example.smarthome.service.ChannelService;
import com.example.smarthome.service.ContentService;
import com.example.smarthome.service.ProgramService;
import com.example.smarthome.service.TvGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("tv_guide")
public class TvGuideController {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ProgramService programService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private TvGuideService tvGuideService;

    @GetMapping("/init")
    public String init() {
        ChannelCreator.getChannels().forEach(channelService::add);
        ProgramCreator.getPrograms().forEach(programService::add);
        ContentCreator.getContents().forEach(contentService::add);

        programService.addProgramToChannel(1L, 1L);
        programService.addProgramToChannel(2L, 2L);
        programService.addProgramToChannel(3L, 3L);

        return "redirect:/tv_guide";
    }

    @GetMapping
    public String tvGuide(Model model) {
        List<ChannelDto> channels = tvGuideService.followedChannelsWithPrograms();
        model.addAttribute("channels", channels);

        return "tv_guide";
    }
}
