package com.example.smarthome.data_sample;

import com.example.smarthome.model.tvGuide.Channel;
import com.example.smarthome.model.tvGuide.Content;
import com.example.smarthome.model.tvGuide.Program;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProgramCreator {
    public static List<Program> getPrograms(List<Content> contents, List<Channel> channels){
        List<Program> programs = new ArrayList<>();

        Content content = getRandomContent(contents);
        Channel channel = getRandomChannel(channels);
        Program program = Program.builder()
                .start(LocalDateTime.of(2021, 7, 11, 12, 55))
                .end(LocalDateTime.of(2021, 7, 11, 15, 20))
                .content(content)
                .channel(channel)
                .build();
        channel.addProgram(program);
        content.addProgram(program);
        programs.add(program);

        content = getRandomContent(contents);
        channel = getRandomChannel(channels);
        program = Program.builder()
                .start(LocalDateTime.of(2021, 7, 11, 13, 20))
                .end(LocalDateTime.of(2021, 7, 11, 16, 0))
                .content(content)
                .channel(channel)
                .build();
        channel.addProgram(program);
        content.addProgram(program);
        programs.add(program);

        content = getRandomContent(contents);
        channel = getRandomChannel(channels);
        program = Program.builder()
                .start(LocalDateTime.of(2021, 7, 11, 13, 0))
                .end(LocalDateTime.of(2021, 7, 11, 13, 45))
                .content(content)
                .channel(channel)
                .build();
        channel.addProgram(program);
        content.addProgram(program);
        programs.add(program);

        return programs;
    }

    private static Content getRandomContent(List<Content> contents) {
        return contents.get((int) (Math.random() * (contents.size() - 1)));
    }

    private static Channel getRandomChannel(List<Channel> channels) {
        return channels.get((int) (Math.random() * (channels.size() - 1)));
    }
}
