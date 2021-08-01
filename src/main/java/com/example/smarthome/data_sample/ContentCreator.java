package com.example.smarthome.data_sample;

import com.example.smarthome.model.tvGuide.Content;

import java.util.ArrayList;
import java.util.List;

public class ContentCreator {
    public static List<Content> getContents(){
        return List.of(
                Content.builder()
                        .title("Movie 1")
                        .description("Description 1")
                        .programs(new ArrayList<>())
                        .build(),
                Content.builder()
                        .title("Sport 1")
                        .description("Description 2")
                        .programs(new ArrayList<>())
                        .build(),
                Content.builder()
                        .title("TV show 1")
                        .description("Description 3")
                        .programs(new ArrayList<>())
                        .build()
        );
    }
}
