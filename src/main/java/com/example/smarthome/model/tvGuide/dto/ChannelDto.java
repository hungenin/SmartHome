package com.example.smarthome.model.tvGuide.dto;

import com.example.smarthome.model.tvGuide.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {
    private Long id;
    private String name;
    private Boolean follow;
    private List<ProgramDto> programs = new ArrayList<>();

    public ChannelDto(Long id, String name, Boolean follow) {
        this.id = id;
        this.name = name;
        this.follow = follow;
    }

    public ChannelDto(Channel channel) {
        id = channel.getId();
        name = channel.getName();
        follow = channel.getFollow();
    }
}
