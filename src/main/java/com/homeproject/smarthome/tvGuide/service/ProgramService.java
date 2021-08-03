package com.homeproject.smarthome.tvGuide.service;

import com.homeproject.smarthome.tvGuide.dao.ProgramDao;
import com.homeproject.smarthome.tvGuide.model.Program;
import com.homeproject.smarthome.tvGuide.model.dto.ProgramDto;
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
}
