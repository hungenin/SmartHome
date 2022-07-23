package com.homeproject.smarthome.tvguide.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
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
    public int compareTo(Program program) {
        if (start.isEqual(program.start)) return 0;
        if (start.isBefore(program.start)) return -1;
        return 1;
    }
}
