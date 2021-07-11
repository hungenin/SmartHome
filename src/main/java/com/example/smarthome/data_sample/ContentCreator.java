package com.example.smarthome.data_sample;

import com.example.smarthome.model.tvGuide.Content;

import java.util.List;

public class ContentCreator {
    public static List<Content> getContents(){
        return List.of(
                Content.builder()
                        .title("Movie 1")
                        .description("Description 1")
                        .build(),
                Content.builder()
                        .title("Sport 1")
                        .description("Description 2")
                        .build(),
                Content.builder()
                        .title("TV show 1")
                        .description("Description 3")
                        .build()
        );
    }
}
