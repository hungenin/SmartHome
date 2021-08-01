package com.example.smarthome.model.tvGuide;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
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
        return id != null && id.equals(channel.id);
    }
}
