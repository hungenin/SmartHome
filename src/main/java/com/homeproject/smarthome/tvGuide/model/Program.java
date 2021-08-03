package com.homeproject.smarthome.tvGuide.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Program implements Comparable<Program> {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Content content;
    private Channel channel;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Program program = (Program) object;
        return id != null && id.equals(program.id);
    }

    @Override
    public int compareTo(Program program) {
        if (start.isEqual(program.start)) return 0;
        if (start.isBefore(program.start)) return -1;
        return 1;
    }
}
