package com.homeproject.smarthome.tvGuide.service;

import com.homeproject.smarthome.tvGuide.dao.ChannelDao;
import com.homeproject.smarthome.tvGuide.dao.ContentDao;
import com.homeproject.smarthome.tvGuide.dao.ProgramDao;
import com.homeproject.smarthome.tvGuide.exception.CannotBeAddedException;
import com.homeproject.smarthome.tvGuide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvGuide.exception.InvalidDataException;
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
        if (!program.getStart().isBefore(program.getEnd())) {
            throw new InvalidDataException("{\"object\" : \"Program\",\"field\" : \"end\", \"rejected value\" : \"" + program.getEnd().toString() + "\", \"message\" : \"Start time must be before end time!\"}");
        }

        Content content = contentDao.findById(program.getContent().getId()).orElseThrow(() -> new CannotBeAddedException("{\"object\" : \"Program\",\"field\" : \"content\", \"rejected value\" : \"" + program.getContent().getId() + "\", \"message\" : \"Content not found!\"}"));
        program.setContent(content);
        if (!content.getPrograms().contains(program)) content.getPrograms().add(program);

        Channel channel = channelDao.findById(program.getChannel().getId()).orElseThrow(() -> new CannotBeAddedException("{\"object\" : \"Program\",\"field\" : \"channel\", \"rejected value\" : \"" + program.getChannel().getId() + "\", \"message\" : \"Channel not found!\"}"));
        program.setChannel(channel);
        if (!channel.getPrograms().contains(program)) channel.getPrograms().add(program);

        program.setId(null);
        return new ProgramDto(programDao.save(program));
    }

    public ProgramDto get(Long id) {
        return new ProgramDto(programDao.findById(id).orElseThrow(DataNotFoundException::new));
    }

    public ProgramDto update(Long id, Program program) {
        if (!program.getStart().isBefore(program.getEnd())) {
            throw new InvalidDataException("{\"object\" : \"Program\",\"field\" : \"end\",\"rejected value\" : \"" + program.getEnd().toString() + "\",\"message\" : \"Start time must be before end time!\"}");
        }

        Content content = contentDao.findById(program.getContent().getId()).orElseThrow(() -> new CannotBeAddedException("{\"object\" : \"Program\",\"field\" : \"content\",\"rejected value\" : \"" + program.getContent().getId() + "\",\"message\" : \"Content not found!\"}"));
        program.setContent(content);
        if (!content.getPrograms().contains(program)) content.getPrograms().add(program);

        Channel channel = channelDao.findById(program.getChannel().getId()).orElseThrow(() -> new CannotBeAddedException("{\"object\" : \"Program\",\"field\" : \"channel\",\"rejected value\" : \"" + program.getChannel().getId() + "\",\"message\" : \"Channel not found!\"}"));
        program.setChannel(channel);
        if (!channel.getPrograms().contains(program)) channel.getPrograms().add(program);

        if (programDao.existsById(id)) {
            program.setId(id);
            return new ProgramDto(programDao.save(program));
        } else {
            throw new DataNotFoundException();
        }
    }

    public void delete(Long id) {
        if (programDao.existsById(id)) {
            programDao.deleteById(id);
        } else {
            throw new DataNotFoundException();
        }
    }

    public List<ProgramDto> programs() {
        return programDao.findAll().stream().map(ProgramDto::new).collect(Collectors.toList());
    }
}
