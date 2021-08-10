package com.homeproject.smarthome.tvGuide.controller.api;

import com.homeproject.smarthome.tvGuide.service.TvGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tv_guide")
public class TvGuideApiController {
    @Autowired
    private TvGuideService tvGuideService;

    @GetMapping("/init")
    public ResponseEntity<?> init() {
        tvGuideService.init();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> tvGuide() {
        return ResponseEntity.ok(tvGuideService.followedChannelsWithPrograms());
    }
}
