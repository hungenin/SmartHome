package com.example.smarthome.service;

import com.example.smarthome.dao.ProgramDao;
import com.example.smarthome.model.tvGuide.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramService {
    @Autowired
    private ProgramDao programDao;

    public void add(Program program) {
        programDao.add(program);
    }

    public Program get(Long id) {
        return programDao.get(id);
    }

    public void update(Program program) {
        programDao.update(program);
    }

    public void delete(Program program) {
        programDao.delete(program);
    }

    public List<Program> programs() {
        return programDao.programs();
    }
}
