package com.example.smarthome.data_sample;

import com.example.smarthome.model.tvGuide.Channel;

import java.util.List;

public class ChannelCreator {
    public static List<Channel> getChannels(){
        return List.of(
                Channel.builder()
                        .name("RTL klub")
                        .follow(true)
                        .build(),
                Channel.builder()
                        .name("M4 sport")
                        .follow(false)
                        .build(),
                Channel.builder()
                        .name("Viasat")
                        .follow(false)
                        .build()
        );
    }
}
