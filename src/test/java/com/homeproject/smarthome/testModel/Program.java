package com.homeproject.smarthome.testModel;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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
        return Objects.equals(id, program.id);
    }

    @Override
    public int compareTo(Program program) {
        if (start.isEqual(program.start)) return 0;
        if (start.isBefore(program.start)) return -1;
        return 1;
    }
}
