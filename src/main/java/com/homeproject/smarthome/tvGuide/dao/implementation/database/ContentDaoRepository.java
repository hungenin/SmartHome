package com.homeproject.smarthome.tvGuide.dao.implementation.database;

import com.homeproject.smarthome.tvGuide.dao.ContentDao;
import com.homeproject.smarthome.tvGuide.model.Content;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContentDaoRepository extends ContentDao, CrudRepository<Content, Long> {
    @Override
    List<Content> findAll();
}
