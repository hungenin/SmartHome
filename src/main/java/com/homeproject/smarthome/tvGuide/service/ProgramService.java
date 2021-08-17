package com.homeproject.smarthome.tvGuide.service;

import com.homeproject.smarthome.tvGuide.dao.ChannelDao;
import com.homeproject.smarthome.tvGuide.dao.ContentDao;
import com.homeproject.smarthome.tvGuide.dao.ProgramDao;
import com.homeproject.smarthome.tvGuide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvGuide.model.Channel;
import com.homeproject.smarthome.tvGuide.model.Content;
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
    @Autowired
    private ContentDao contentDao;
    @Autowired
    private ChannelDao channelDao;

    public ProgramDto add(Program program) {
        Content content = contentDao.findById(program.getContent().getId()).get();
        program.setContent(content);
        if (!content.getPrograms().contains(program)) content.getPrograms().add(program);

        Channel channel = channelDao.findById(program.getChannel().getId()).get();
        program.setChannel(channel);
        if (!channel.getPrograms().contains(program)) channel.getPrograms().add(program);

        program.setId(null);
        return new ProgramDto(programDao.save(program));
    }

    public ProgramDto get(Long id) {
        return new ProgramDto(programDao.findById(id).orElseThrow(DataNotFoundException::new));
    }

    public ProgramDto update(Long id, Program program) {
        if (programDao.existsById(id)) {
            program.setId(id);
            return new ProgramDto(programDao.save(program));
        } else {
            throw new DataNotFoundException();
        }
    }

    public void delete(Long id) {
        if (programDao.existsById(id)) {
            programDao.deleteById(Program.builder().id(id).build());
        } else {
            throw new DataNotFoundException();
        }
    }

    public List<ProgramDto> programs() {
        return programDao.findAll().stream().map(ProgramDto::new).collect(Collectors.toList());
    }
}
