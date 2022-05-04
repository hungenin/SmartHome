package com.homeproject.smarthome.tvGuide.dao;

import com.homeproject.smarthome.tvGuide.model.Program;
import java.util.List;
import java.util.Optional;

public interface ProgramDao {
    Program save(Program program);
    Optional<Program> findById(Long id);
    void deleteById(Long id);
    List<Program> findAll();
    boolean existsById(Long id);
}
