package com.homeproject.smarthome.tvGuide.dao.implementation.database;

import com.homeproject.smarthome.tvGuide.dao.ChannelDao;
import com.homeproject.smarthome.tvGuide.model.Channel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChannelDaoRepository extends ChannelDao, CrudRepository<Channel, Long> {
    @Override
    List<Channel> findAll();

    @Override
    List<Channel> findChannelsByFollowEquals(boolean isFollow);

    @Override
    @Transactional
    @Modifying
    @Query("UPDATE channel c SET c.follow = ?2 WHERE c.id = ?1")
    void setFollow(Long id, boolean follow);
}
