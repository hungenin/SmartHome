package com.homeproject.smarthome.tvguide.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "channel")
public class Channel {
    @Id
    @NotNull
    private Short id;
    @NotBlank
    @NotNull
    private String name;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String logo;
    @NotNull
    private Boolean follow;
    @JsonIgnore
    @OneToMany(mappedBy = "channel")
    private List<Program> programs = new ArrayList<>();
}
