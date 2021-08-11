package com.homeproject.smarthome.tvGuide.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Entity
public class Channel {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "channel")
    private List<Program> programs;
}