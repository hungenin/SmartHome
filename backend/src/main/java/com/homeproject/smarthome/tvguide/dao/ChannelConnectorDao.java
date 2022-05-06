package com.homeproject.smarthome.tvguide.dao;

import com.homeproject.smarthome.tvguide.model.ChannelConnector;

import java.util.List;
import java.util.Optional;

public interface ChannelConnectorDao {
    ChannelConnector save(ChannelConnector channelConnector);
    Optional<ChannelConnector> findByChannelId(Long chanelId);
    Optional<ChannelConnector> findByPortId(Integer portId);
    void deleteByChannelId(Long channelId);
    void deleteByPortId(Integer portId);
    List<ChannelConnector> findAll();
    boolean existsByChannelId(Long channelId);
    boolean existsByPortId(Integer portId);
}
