package com.homeproject.smarthome.tvGuide.data_sample;

import com.homeproject.smarthome.tvGuide.model.Channel;

import java.util.ArrayList;
import java.util.List;

public class ChannelCreator {
    public static List<Channel> getChannels(){
        return List.of(
                Channel.builder()
                        .name("RTL klub")
                        .follow(true)
                        .programs(new ArrayList<>())
                        .build(),
                Channel.builder()
                        .name("M4 sport")
                        .follow(false)
                        .programs(new ArrayList<>())
                        .build(),
                Channel.builder()
                        .name("Viasat")
                        .follow(false)
                        .programs(new ArrayList<>())
                        .build()
        );
    }
}
