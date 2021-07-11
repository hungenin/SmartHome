package com.example.smarthome.model.tvGuide;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Channel {
    @JsonIgnore
    private Long id;
    private String name;
    private Boolean follow;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Channel channel = (Channel) object;
        return id.equals(channel.id);
    }
}
