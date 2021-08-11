package com.homeproject.smarthome.tvGuide.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Program {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    @ManyToOne
    private Channel channel;
    @ManyToOne
    private Content content;
}