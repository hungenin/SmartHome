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

    public void update(Channel channel) {
        channelDao.update(channel);
    }

    public void delete(Channel channel) {
        channelDao.delete(channel);
    }

    public List<Channel> channels() {
        return channelDao.channels();
    }

    public List<Channel> followedChannels() {
        return channelDao.followedChannels();
    }

    public void setFollow(Channel channel) {
        channelDao.setFollow(channel);
    }
}
