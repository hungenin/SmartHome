package com.homeproject.smarthome.tvGuide.service;

import com.homeproject.smarthome.tvGuide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvGuide.dao.ProgramRepository;
import com.homeproject.smarthome.tvGuide.model.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProgramService {
    @Autowired
    private ProgramRepository programRepository;

    public Program add(Program program) {
        program.setId(null);
        return programRepository.save(program);
    }
    
    public Program updateById(Long id, Program program) {
        if (programRepository.existsById(id)) {
            program.setId(id);
            return programRepository.save(program);
        } else {
            throw new DataNotFoundException();
        }
    }

    public Program findById(Long id) {
        return programRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }

    public List<Program> findAll() {
        List<Program> list = new ArrayList<>();
        programRepository.findAll().forEach(list::add);
        return list;
    }

    public void deleteById(Long id) {
        if (programRepository.existsById(id)) {
            programRepository.deleteById(id);
        } else {
            throw new DataNotFoundException();
        }
    }
}
