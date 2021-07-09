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

    public void addChannel(Channel channel) {
        channelDao.addChannel(channel);
    }

    public Channel getChannelById(Long channelId) {
        return channelDao.getChannelById(channelId);
    }

    public void updateChannelById(Long channelId, Channel channel) {
        channelDao.updateChannelById(channelId, channel);
    }

    public void deleteChannelById(Long channelId) {
        channelDao.deleteChannelById(channelId);
    }

    public List<Channel> channels() {
        return channelDao.channels();
    }

    public List<Channel> followedChannels() {
        return channelDao.followedChannels();
    }

    public void updateFollowById(Channel channel) {
        channelDao.updateFollowById(channel);
    }
}
