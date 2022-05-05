package com.homeproject.smarthome.tvguide.controller.api;

import com.homeproject.smarthome.tvguide.service.TvGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tv_guide")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TvGuideApiController {
    @Autowired
    private TvGuideService tvGuideService;

    @GetMapping
    public ResponseEntity<?> tvGuide() {
        return ResponseEntity.ok(tvGuideService.followedChannelsWithPrograms());
    }
}
