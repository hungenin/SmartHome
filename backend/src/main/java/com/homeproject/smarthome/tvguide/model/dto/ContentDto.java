package com.homeproject.smarthome.tvguide.model.dto;

import com.homeproject.smarthome.tvguide.model.Content;
import lombok.Getter;

@Getter
public class ContentDto {
    private final Long id;
    private final String title;
    private final String description;

    public ContentDto(Content content) {
        id = content.getId();
        title = content.getTitle();
        description = content.getDescription();
    }
}
