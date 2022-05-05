package com.homeproject.smarthome.tvguide.service;

import com.homeproject.smarthome.tvguide.dao.ChannelDao;
import com.homeproject.smarthome.tvguide.dao.ContentDao;
import com.homeproject.smarthome.tvguide.dao.ProgramDao;
import com.homeproject.smarthome.tvguide.datagrabber.DataGrabber;
import com.homeproject.smarthome.tvguide.exception.CannotBeAddedException;
import com.homeproject.smarthome.tvguide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvguide.exception.InvalidDataException;
import com.homeproject.smarthome.tvguide.model.Channel;
import com.homeproject.smarthome.tvguide.model.Content;
import com.homeproject.smarthome.tvguide.model.Program;
import com.homeproject.smarthome.tvguide.model.dto.ProgramDto;
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
    @Autowired
    private DataGrabber dataGrabber;

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

    public List<ProgramDto> programs() {
        return programDao.findAll().stream().map(ProgramDto::new).collect(Collectors.toList());
    }

    public void refreshPrograms() {
        dataGrabber.refreshChannels();
    }
}
