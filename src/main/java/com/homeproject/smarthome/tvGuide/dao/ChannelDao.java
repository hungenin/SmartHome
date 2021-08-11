package com.homeproject.smarthome.tvGuide.dao;

import com.homeproject.smarthome.tvGuide.model.Channel;
import java.util.List;
import java.util.Optional;

public interface ChannelDao {
    Channel add(Channel channel);
    Optional<Channel> get(Long id);
    Channel update(Channel channel);
    void delete(Channel channel);
    List<Channel> channels();
    boolean existsById(Long id);

    List<Channel> followedChannels();
    void setFollow(Channel channel);
}
