package com.example.smarthome.service;

import com.example.smarthome.dao.ProgramDao;
import com.example.smarthome.model.tvGuide.Program;
import com.example.smarthome.model.tvGuide.dto.ProgramDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramService {
    @Autowired
    private ProgramDao programDao;

    public void add(Program program) {
        program.setId(null);
        programDao.add(program);
    }

    public ProgramDto get(Long id) {
        return new ProgramDto(programDao.get(id));
    }

    public void update(Long id, Program program) {
        program.setId(id);
        programDao.update(program);
    }

    public void delete(Long id) {
        programDao.delete(Program.builder().id(id).build());
    }

    public List<ProgramDto> programs() {
        return programDao.programs().stream().map(ProgramDto::new).collect(Collectors.toList());
    }

    public List<Program> programsByChannel(Long id) {
        return programDao.programsByChannel(id);
    }

    public void addProgramToChannel(Long programId, Long channelId) {
        programDao.addProgramToChannel(programId, channelId);
    }
}
