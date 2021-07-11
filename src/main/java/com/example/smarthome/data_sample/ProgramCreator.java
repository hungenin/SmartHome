package com.example.smarthome.data_sample;

import com.example.smarthome.model.tvGuide.Program;

import java.time.LocalDateTime;
import java.util.List;

public class ProgramCreator {
    public static List<Program> getPrograms(){
        return List.of(
                Program.builder()
                        .start(LocalDateTime.of(2021, 7, 11, 12, 55))
                        .end(LocalDateTime.of(2021, 7, 11, 15, 20))
                        .build(),
                Program.builder()
                        .start(LocalDateTime.of(2021, 7, 11, 13, 20))
                        .end(LocalDateTime.of(2021, 7, 11, 16, 0))
                        .build(),
                Program.builder()
                        .start(LocalDateTime.of(2021, 7, 11, 13, 0))
                        .end(LocalDateTime.of(2021, 7, 11, 13, 45))
                        .build()
        );
    }
}
