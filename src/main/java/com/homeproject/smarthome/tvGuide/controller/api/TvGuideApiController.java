package com.homeproject.smarthome.tvGuide.controller.api;

import com.homeproject.smarthome.tvGuide.model.dto.ChannelDto;
import com.homeproject.smarthome.tvGuide.service.TvGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/tv_guide")
public class TvGuideApiController {
    @Autowired
    private TvGuideService tvGuideService;

    @GetMapping("/init")
    public void init() {
        tvGuideService.init();
    }

    @GetMapping
    public List<ChannelDto> tvGuide() {
        return tvGuideService.followedChannelsWithPrograms();
    }
}
