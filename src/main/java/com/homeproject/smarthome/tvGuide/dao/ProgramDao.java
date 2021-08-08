package com.homeproject.smarthome.tvGuide.dao;

import com.homeproject.smarthome.tvGuide.model.Program;
import java.util.List;

public interface ProgramDao {
    Program add(Program program);
    Program get(Long id);
    Program update(Program program);
    void delete(Program program);
    List<Program> programs();
}
