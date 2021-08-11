package com.homeproject.smarthome.testModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    private Long id;
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotBlank
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
        return Objects.equals(id, content.id);
    }
}
