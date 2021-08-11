package com.homeproject.smarthome.tvGuide.service;

import com.homeproject.smarthome.tvGuide.dao.ChannelDao;
import com.homeproject.smarthome.tvGuide.dao.ContentDao;
import com.homeproject.smarthome.tvGuide.dao.ProgramDao;
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
        Content content = contentDao.get(program.getContent().getId());
        program.setContent(content);
        if (!content.getPrograms().contains(program)) content.getPrograms().add(program);

        Channel channel = channelDao.get(program.getChannel().getId());
        program.setChannel(channel);
        if (!channel.getPrograms().contains(program)) channel.getPrograms().add(program);

        program.setId(null);
        return new ProgramDto(programDao.add(program));
    }

    public ProgramDto get(Long id) {
        return new ProgramDto(programDao.get(id));
    }

    public ProgramDto update(Long id, Program program) {
        program.setId(id);
        return new ProgramDto(programDao.update(program));
    }

    public void delete(Long id) {
        programDao.delete(Program.builder().id(id).build());
    }

    public List<ProgramDto> programs() {
        return programDao.programs().stream().map(ProgramDto::new).collect(Collectors.toList());
    }
}
