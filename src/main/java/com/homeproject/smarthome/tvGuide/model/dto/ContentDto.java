package com.homeproject.smarthome.tvGuide.model.dto;

import com.homeproject.smarthome.tvGuide.model.Content;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContentDto {
    private Long id;
    private String title;
    private String description;

    public ContentDto(Content content) {
        id = content.getId();
        title = content.getTitle();
        description = content.getDescription();
    }
}
