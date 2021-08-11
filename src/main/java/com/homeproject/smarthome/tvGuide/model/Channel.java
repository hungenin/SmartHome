package com.homeproject.smarthome.tvGuide.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    private Long id;
    private String name;
    private Boolean follow;
    @JsonIgnore
    private List<Program> programs;

    public void addProgram(Program program) {
        if (program != null && !programs.contains(program)) programs.add(program);
    }

    public void removeProgram(Program program) {
        programs.removeIf(program::equals);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Channel channel = (Channel) object;
        return Objects.equals(id, channel.id);
    }
}
