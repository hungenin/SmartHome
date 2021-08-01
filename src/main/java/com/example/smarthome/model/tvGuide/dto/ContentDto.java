package com.example.smarthome.model.tvGuide.dto;

import com.example.smarthome.model.tvGuide.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
