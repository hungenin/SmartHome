package com.example.smarthome.service;

import com.example.smarthome.dao.ChannelDao;
import com.example.smarthome.model.tvGuide.Channel;
import com.example.smarthome.model.tvGuide.dto.ChannelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;

    public void add(Channel channel) {
        channelDao.add(channel);
    }

    public ChannelDto get(Long id) {
        return new ChannelDto(channelDao.get(id));
    }

    public void update(Long id, Channel channel) {
        channel.setId(id);
        channelDao.update(channel);
    }

    public void delete(Long id) {
        channelDao.delete(Channel.builder().id(id).build());
    }

    public List<ChannelDto> channels() {
        return channelDao.channels().stream().map(ChannelDto::new).collect(Collectors.toList());
    }

    public List<ChannelDto> followedChannels() {
        return channelDao.followedChannels().stream().map(ChannelDto::new).collect(Collectors.toList());
    }

    public void setFollow(Long id) {
        channelDao.setFollow(Channel.builder().id(id).follow(true).build());
    }
    public void setUnFollow(Long id) {
        channelDao.setFollow(Channel.builder().id(id).follow(false).build());
    }
}
