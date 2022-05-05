package com.homeproject.smarthome.tvguide.dao.implementation.database;

import com.homeproject.smarthome.tvguide.dao.ContentDao;
import com.homeproject.smarthome.tvguide.model.Content;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContentDaoRepository extends ContentDao, CrudRepository<Content, Long> {
    @Override
    List<Content> findAll();
}
