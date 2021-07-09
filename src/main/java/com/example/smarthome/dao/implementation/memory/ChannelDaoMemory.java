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
    private AtomicLong id = new AtomicLong(1);
    private List<Channel> channels = new ArrayList<>();

    @Override
    public void addChannel(Channel channel) {
        channel.setId(id.getAndIncrement());
        //TODO add if not exists
        channels.add(channel);
    }

    @Override
    public Channel getChannelById(Long channelId) {
        return channels.stream()
                .filter(channel -> channel.getId().equals(channelId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateChannelById(Long channelId, Channel channel) {
        channels.stream()
                .filter(channel1 -> channel1.getId().equals(channelId))
                .forEach(channel1 -> {
                    channel1.setName(channel.getName());
                    channel1.setFollow(channel.getFollow());
                });
    }

    @Override
    public void deleteChannelById(Long channelId) {
        channels.removeIf(channel -> channel.getId().equals(channelId));
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
    public void updateFollowById(Channel channel) {
        channels.stream()
                .filter(channel1 -> channel1.equals(channel))
                .forEach(channel1 -> {
                    channel1.setFollow(channel.getFollow());
                });
    }
}
