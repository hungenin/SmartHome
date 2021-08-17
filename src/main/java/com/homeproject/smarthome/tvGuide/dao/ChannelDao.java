package com.homeproject.smarthome.tvGuide.dao;

import com.homeproject.smarthome.tvGuide.model.Channel;
import java.util.List;
import java.util.Optional;

public interface ChannelDao {
    Channel save(Channel channel);
    Optional<Channel> findById(Long id);
    void deleteById(Long id);
    List<Channel> findAll();
    boolean existsById(Long id);

    List<Channel> findChannelsByFollowEquals(boolean isFollow);
    void setFollow(Long id, boolean follow);
}
