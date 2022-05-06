package com.homeproject.smarthome.tvguide.model.dto;

import com.homeproject.smarthome.tvguide.model.Channel;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {
    private Long id;
    private String name;
    private String logo;
    private Boolean follow;
    private List<ProgramDto> programs = new ArrayList<>();

    public ChannelDto(Long id, String name, String logo, Boolean follow) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.follow = follow;
    }

    public ChannelDto(Channel channel) {
        id = channel.getId();
        name = channel.getName();
        logo = channel.getLogo();
        follow = channel.getFollow();
        programs = channel.getPrograms() == null ? null : channel.getPrograms().stream().map(ProgramDto::new).collect(Collectors.toList());
    }
}
