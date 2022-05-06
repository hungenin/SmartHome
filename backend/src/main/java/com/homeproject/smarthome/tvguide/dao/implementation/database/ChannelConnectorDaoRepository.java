package com.homeproject.smarthome.tvguide.dao.implementation.database;

import com.homeproject.smarthome.tvguide.dao.ChannelConnectorDao;
import com.homeproject.smarthome.tvguide.model.ChannelConnector;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChannelConnectorDaoRepository extends ChannelConnectorDao, CrudRepository<ChannelConnector, Long> {
    List<ChannelConnector> findAll();
}
