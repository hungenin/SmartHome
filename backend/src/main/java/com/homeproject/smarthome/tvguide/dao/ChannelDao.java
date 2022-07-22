package com.homeproject.smarthome.tvguide.dao;

import com.homeproject.smarthome.tvguide.model.Channel;
import java.util.List;
import java.util.Optional;

public interface ChannelDao {
    Channel save(Channel channel);
    Optional<Channel> findById(Short id);
    void deleteById(Short id);
    List<Channel> findAll();
    boolean existsById(Short id);

    List<Channel> findChannelsByFollowEquals(boolean isFollow);
    void setFollow(Short id, boolean follow);
    void setAllFollow(boolean follow);
}
