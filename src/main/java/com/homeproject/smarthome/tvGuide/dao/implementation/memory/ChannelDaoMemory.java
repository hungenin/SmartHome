package com.homeproject.smarthome.tvGuide.dao.implementation.memory;

import com.homeproject.smarthome.tvGuide.dao.ChannelDao;
import com.homeproject.smarthome.tvGuide.model.Channel;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ChannelDaoMemory implements ChannelDao {
    private final AtomicLong idCounter = new AtomicLong(1);
    private final List<Channel> channels = new ArrayList<>();

    @Override
    public Channel add(Channel channel) {
        if (channel.getId() == null || !channels.contains(channel)){
            channel.setId(idCounter.getAndIncrement());
            channels.add(channel.toBuilder().build());
        }

        return channel;
    }

    @Override
    public Optional<Channel> get(Long id) {
        return channels.stream()
                .filter(channel -> channel.getId().equals(id))
                .map(channel -> channel.toBuilder().build())
                .findFirst();
    }

    @Override
    public Channel update(Channel channel) {
        channels.stream()
                .filter(channel::equals)
                .forEach(channel1 -> {
                    channel1.setName(channel.getName());
                    channel1.setFollow(channel.getFollow());
                    channel1.setPrograms(channel.getPrograms());
                });

        return channel;
    }

    @Override
    public void delete(Channel channel) {
        channels.removeIf(channel1 -> channel1.equals(channel) && (channel1.getPrograms() == null || channel1.getPrograms().size() == 0));
    }

    @Override
    public List<Channel> channels() {
        return new ArrayList<>(channels);
    }

    @Override
    public boolean existsById(Long id) {
        for (Channel channel : channels) {
            if (channel.getId().equals(id)) return true;
        }
        return false;
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
