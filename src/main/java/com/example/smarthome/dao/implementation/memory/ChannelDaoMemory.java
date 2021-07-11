package com.example.smarthome.dao.implementation.memory;

import com.example.smarthome.dao.ChannelDao;
import com.example.smarthome.model.tvGuide.Channel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ChannelDaoMemory implements ChannelDao {
    private AtomicLong idCounter = new AtomicLong(1);
    private List<Channel> channels = new ArrayList<>();

    @Override
    public void add(Channel channel) {
        if (channel.getId() == null || !channels.contains(channel)){
            channel.setId(idCounter.getAndIncrement());
            channels.add(channel.toBuilder().build());
        }
    }

    @Override
    public Channel get(Long id) {
        return channels.stream()
                .filter(channel -> channel.getId().equals(id))
                .map(channel -> channel.toBuilder().build())
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Channel channel) {
        channels.stream()
                .filter(channel::equals)
                .forEach(channel1 -> {
                    channel1.setName(channel.getName());
                    channel1.setFollow(channel.getFollow());
                });
    }

    @Override
    public void delete(Channel channel) {
        channels.removeIf(channel::equals);
    }

    @Override
    public List<Channel> channels() {
        return new ArrayList<>(channels);
    }

    @Override
    public List<Channel> followedChannels() {
        return channels.stream()
                .filter(Channel::getFollow)
                .collect(Collectors.toList());
    }

    @Override
    public void setFollow(Channel channel) {
        channels.stream()
                .filter(channel::equals)
                .forEach(channel1 -> channel1.setFollow(channel.getFollow()));
    }
}
