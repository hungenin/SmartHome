package com.homeproject.smarthome.testModel.dto;

import com.homeproject.smarthome.testModel.Content;
import com.homeproject.smarthome.testModel.Program;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Content content;

    public ProgramDto(Program program) {
        id = program.getId();
        start = program.getStart();
        end = program.getEnd();
        content = program.getContent();
    }
}
