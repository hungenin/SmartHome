package com.homeproject.smarthome.tvguide.service;

import com.homeproject.smarthome.tvguide.dao.ProgramDao;
import com.homeproject.smarthome.tvguide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvguide.model.dto.ProgramDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramService {
    @Autowired
    private ProgramDao programDao;

    public ProgramDto get(Long id) {
        return new ProgramDto(programDao.findById(id).orElseThrow(DataNotFoundException::new));
    }

    public List<ProgramDto> programs() {
        return programDao.findAll().stream().map(ProgramDto::new).collect(Collectors.toList());
    }
}
