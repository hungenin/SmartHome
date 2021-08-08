package com.homeproject.smarthome.tvGuide.dao;

import com.homeproject.smarthome.tvGuide.model.Channel;

import java.util.List;

public interface ChannelDao {
    Channel add(Channel channel);
    Channel get(Long id);
    Channel update(Channel channel);
    void delete(Channel channel);
    List<Channel> channels();

    List<Channel> followedChannels();
    void setFollow(Channel channel);
}
