package com.homeproject.smarthome.tvguide.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Program implements Comparable<Program> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @ManyToOne
    @NotNull
    private Content content;
    @ManyToOne
    @NotNull
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
