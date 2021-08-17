package com.homeproject.smarthome.tvGuide.service;

import com.homeproject.smarthome.tvGuide.dao.ChannelDao;
import com.homeproject.smarthome.tvGuide.exception.CannotBeDeletedException;
import com.homeproject.smarthome.tvGuide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvGuide.model.Channel;
import com.homeproject.smarthome.tvGuide.model.dto.ChannelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;

    public ChannelDto add(Channel channel) {
        channel.setId(null);
        return new ChannelDto(channelDao.save(channel));
    }

    public ChannelDto get(Long id) {
        return new ChannelDto(channelDao.findById(id).orElseThrow(DataNotFoundException::new));
    }

    public ChannelDto update(Long id, Channel channel) {
        if (channelDao.existsById(id)) {
            channel.setId(id);
            return new ChannelDto(channelDao.save(channel));
        } else {
            throw new DataNotFoundException();
        }
    }

    public void delete(Long id) {
        Channel channel = channelDao.findById(id).orElseThrow(DataNotFoundException::new);

        if (channel.getPrograms().isEmpty()) {
            channelDao.deleteById(id);
        } else {
            throw new CannotBeDeletedException();
        }
    }

    public List<ChannelDto> channels() {
        return channelDao.findAll().stream().map(ChannelDto::new).collect(Collectors.toList());
    }

    public List<ChannelDto> followedChannels() {
        return channelDao.findChannelsByFollowEquals(true).stream().map(ChannelDto::new).collect(Collectors.toList());
    }

    public void setFollow(Long id) {
        channelDao.setFollow(id, true);
    }
    public void setUnFollow(Long id) {
        channelDao.setFollow(id, false);
    }
}
