package com.example.smarthome.dao;

import com.example.smarthome.model.tvGuide.Channel;

import java.util.List;

public interface ChannelDao {
    void addChannel(Channel channel);
    Channel getChannelById(Long channelId);
    void updateChannelById(Long channelId, Channel channel);
    void deleteChannelById(Long channelId);
    List<Channel> channels();
    List<Channel> followedChannels();
}
