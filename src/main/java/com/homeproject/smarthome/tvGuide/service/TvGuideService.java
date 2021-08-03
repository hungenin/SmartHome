package com.homeproject.smarthome.tvGuide.service;

import com.homeproject.smarthome.tvGuide.dao.ChannelDao;
import com.homeproject.smarthome.tvGuide.dao.ContentDao;
import com.homeproject.smarthome.tvGuide.dao.ProgramDao;
import com.homeproject.smarthome.tvGuide.data_sample.ChannelCreator;
import com.homeproject.smarthome.tvGuide.data_sample.ContentCreator;
import com.homeproject.smarthome.tvGuide.data_sample.ProgramCreator;
import com.homeproject.smarthome.tvGuide.model.dto.ChannelDto;
import com.homeproject.smarthome.tvGuide.model.dto.ProgramDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TvGuideService {
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private ProgramDao programDao;
    @Autowired
    private ContentDao contentDao;

    public List<ChannelDto> followedChannelsWithPrograms() {
        return channelDao.followedChannels().stream()
                .map(channel -> ChannelDto.builder()
                        .id(channel.getId())
                        .name(channel.getName())
                        .follow(channel.getFollow())
                        .programs(channel.getPrograms()
                                .stream()
                                .map(ProgramDto::new)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    public void init() {
        ContentCreator.getContents().forEach(contentDao::add);
        ChannelCreator.getChannels().forEach(channelDao::add);
        ProgramCreator.getPrograms(contentDao.contents(), channelDao.channels()).forEach(programDao::add);
    }
}
