package com.homeproject.smarthome.tvGuide.model.dto;

import com.homeproject.smarthome.tvGuide.model.Content;
import com.homeproject.smarthome.tvGuide.model.Program;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
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