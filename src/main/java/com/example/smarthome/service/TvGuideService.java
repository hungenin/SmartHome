package com.example.smarthome.service;

import com.example.smarthome.dao.ChannelDao;
import com.example.smarthome.dao.ProgramDao;
import com.example.smarthome.model.tvGuide.Channel;
import com.example.smarthome.model.tvGuide.Program;
import com.example.smarthome.model.tvGuide.dto.ChannelDto;
import com.example.smarthome.model.tvGuide.dto.ProgramDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TvGuideService {
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private ProgramDao programDao;

    public List<ChannelDto> followedChannelsWithPrograms() {
        return channelDao.followedChannels().stream()
                .map(channel -> ChannelDto.builder()
                        .id(channel.getId())
                        .name(channel.getName())
                        .follow(channel.getFollow())
                        .programs(programDao.programsByChannel(channel.getId())
                                .stream()
                                .map(ProgramDto::new)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
