package com.homeproject.smarthome.tvguide.model.dto;

import com.homeproject.smarthome.tvguide.model.Channel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ChannelDto {
    private final Short id;
    private final String name;
    private final String logo;
    private final Boolean follow;
    private final List<ProgramDto> programs;

    public ChannelDto(Channel channel) {
        id = channel.getId();
        name = channel.getName();
        logo = channel.getLogo();
        follow = channel.getFollow();
        programs = channel.getPrograms() == null ? new ArrayList<>() : channel.getPrograms().stream().map(ProgramDto::new).collect(Collectors.toList());
    }
}
