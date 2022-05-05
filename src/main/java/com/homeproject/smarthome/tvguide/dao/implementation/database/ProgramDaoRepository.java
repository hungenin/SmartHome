package com.homeproject.smarthome.tvguide.dao.implementation.database;

import com.homeproject.smarthome.tvguide.dao.ProgramDao;
import com.homeproject.smarthome.tvguide.model.Program;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProgramDaoRepository extends ProgramDao, CrudRepository<Program, Long> {
    @Override
    List<Program> findAll();
}
