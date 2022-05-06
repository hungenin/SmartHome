package com.homeproject.smarthome.tvguide.service;

import com.homeproject.smarthome.tvguide.dao.ChannelDao;
import com.homeproject.smarthome.tvguide.datagrabber.DataGrabber;
import com.homeproject.smarthome.tvguide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvguide.model.Channel;
import com.homeproject.smarthome.tvguide.model.dto.ChannelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private DataGrabber dataGrabber;

    public ChannelDto get(Long id) {
        return new ChannelDto(channelDao.findById(id).orElseThrow(DataNotFoundException::new));
    }

    public ChannelDto update(Channel channel) {
        if (channelDao.existsById(channel.getId())) {
            return new ChannelDto(channelDao.save(channel));
        } else {
            throw new DataNotFoundException();
        }
    }

    public List<ChannelDto> channels() {
        List<Channel> channels = channelDao.findAll();

        if (channels.isEmpty()) {
            dataGrabber.updateChannelList();
            channels = channelDao.findAll();
        }

        return channels.stream().map(ChannelDto::new).collect(Collectors.toList());
    }

    public List<ChannelDto> followedChannels() {
        return channelDao.findChannelsByFollowEquals(true).stream().map(ChannelDto::new).collect(Collectors.toList());
    }

    public void setAllFollow() {
        channelDao.setAllFollow(true);
    }
    public void setAllUnFollow() {
        channelDao.setAllFollow(false);
    }

    public void updateChannelList() {
        dataGrabber.updateChannelList();
    }
}
