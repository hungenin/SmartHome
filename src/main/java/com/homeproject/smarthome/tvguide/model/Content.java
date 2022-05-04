package com.homeproject.smarthome.tvGuide.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotBlank
    private String description;
    @JsonIgnore
    @OneToMany(mappedBy = "content")
    private List<Program> programs = new ArrayList<>();

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
