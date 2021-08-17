package com.homeproject.smarthome.tvGuide.dao.implementation.database;

import com.homeproject.smarthome.tvGuide.dao.ProgramDao;
import com.homeproject.smarthome.tvGuide.model.Program;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProgramDaoRepository extends ProgramDao, CrudRepository<Program, Long> {
    @Override
    List<Program> findAll();
}
