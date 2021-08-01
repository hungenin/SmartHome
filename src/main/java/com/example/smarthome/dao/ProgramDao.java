package com.example.smarthome.dao;

import com.example.smarthome.model.tvGuide.Program;
import java.util.List;

public interface ProgramDao {
    void add(Program program);
    Program get(Long id);
    void update(Program program);
    void delete(Program program);
    List<Program> programs();

    void addProgramToChannel(Long programId, Long channelId);
    List<Program> programsByChannel(Long id);
}
