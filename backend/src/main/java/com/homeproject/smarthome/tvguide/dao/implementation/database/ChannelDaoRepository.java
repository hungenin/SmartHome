package com.homeproject.smarthome.tvguide.dao.implementation.database;

import com.homeproject.smarthome.tvguide.dao.ChannelDao;
import com.homeproject.smarthome.tvguide.model.Channel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChannelDaoRepository extends ChannelDao, CrudRepository<Channel, Short> {
    @Override
    List<Channel> findAll();

    @Override
    List<Channel> findChannelsByFollowEquals(boolean isFollow);

    @Override
    @Transactional
    @Modifying
    @Query("UPDATE channel c SET c.follow = ?2 WHERE c.id = ?1")
    void setFollow(Short id, boolean follow);

    @Override
    @Transactional
    @Modifying
    @Query("UPDATE channel c SET c.follow = ?1")
    void setAllFollow(boolean follow);
}
