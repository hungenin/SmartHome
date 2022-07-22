package com.homeproject.smarthome.tvguide.model.dto;

import com.homeproject.smarthome.tvguide.model.Content;
import com.homeproject.smarthome.tvguide.model.Program;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProgramDto {
    private final Long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Content content;

    public ProgramDto(Program program) {
        id = program.getId();
        start = program.getStart();
        end = program.getEnd();
        content = program.getContent();
    }
}
