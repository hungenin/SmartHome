package com.example.smarthome.model.tvGuide;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class Content {
    private Long id;
    private String title;
    private String description;
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

        Content content = (Content) object;
        return id != null && id.equals(content.id);
    }
}
