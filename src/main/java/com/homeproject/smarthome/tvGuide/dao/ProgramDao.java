package com.homeproject.smarthome.tvGuide.dao;

import com.homeproject.smarthome.tvGuide.model.Program;
import java.util.List;

public interface ProgramDao {
    void add(Program program);
    Program get(Long id);
    void update(Program program);
    void delete(Program program);
    List<Program> programs();
}
