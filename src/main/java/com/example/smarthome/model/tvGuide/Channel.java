package com.example.smarthome.model.tvGuide;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Channel {
    private Long id;
    private String name;
    private Boolean follow;
}
