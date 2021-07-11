package com.example.smarthome.model.tvGuide;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Program implements Comparable<Program> {
    Long id;
    LocalDateTime start;
    LocalDateTime end;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Program program = (Program) object;
        return id.equals(program.id);
    }

    @Override
    public int compareTo(Program program) {
        if (start.isEqual(program.start)) return 0;
        if (start.isBefore(program.start)) return -1;
        return 1;
    }
}
