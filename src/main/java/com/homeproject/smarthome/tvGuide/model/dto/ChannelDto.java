package com.homeproject.smarthome.tvGuide.model.dto;

import com.homeproject.smarthome.tvGuide.model.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        programs = channel.getPrograms() == null ? null : channel.getPrograms().stream().map(ProgramDto::new).collect(Collectors.toList());
    }
}
