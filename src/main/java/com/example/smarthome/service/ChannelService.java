package com.example.smarthome.service;

import com.example.smarthome.dao.ChannelDao;
import com.example.smarthome.model.tvGuide.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;

    public void add(Channel channel) {
        channelDao.add(channel);
    }

    public Channel get(Long id) {
        return channelDao.get(id);
    }

    public void update(Long id, Channel channel) {
        channel.setId(id);
        channelDao.update(channel);
    }

    public void delete(Long id) {
        channelDao.delete(Channel.builder().id(id).build());
    }

    public List<Channel> channels() {
        return channelDao.channels();
    }

    public List<Channel> followedChannels() {
        return channelDao.followedChannels();
    }

    public void setFollow(Long id) {
        channelDao.setFollow(Channel.builder().id(id).follow(true).build());
    }
    public void setUnFollow(Long id) {
        channelDao.setFollow(Channel.builder().id(id).follow(false).build());
    }
}
