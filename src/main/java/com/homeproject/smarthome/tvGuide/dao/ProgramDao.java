package com.homeproject.smarthome.tvGuide.dao;

import com.homeproject.smarthome.tvGuide.model.Program;
import java.util.List;
import java.util.Optional;

public interface ProgramDao {
    Program add(Program program);
    Optional<Program> get(Long id);
    Program update(Program program);
    void delete(Program program);
    List<Program> programs();
    boolean existsById(Long id);
}
