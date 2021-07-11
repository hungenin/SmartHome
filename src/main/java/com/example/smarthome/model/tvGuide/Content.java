package com.example.smarthome.model.tvGuide;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Content {
    private Long id;
    private String title;
    private String description;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Content content = (Content) object;
        return id.equals(content.id);
    }
}
