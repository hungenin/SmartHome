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
    public Channel save(Channel channel) {
        if (channel.getId() == null || !channels.contains(channel)){
            channel.setId(idCounter.getAndIncrement());
            channels.add(channel.toBuilder().build());
        } else {
            channels.stream()
                    .filter(channel::equals)
                    .forEach(channel1 -> {
                        channel1.setName(channel.getName());
                        channel1.setFollow(channel.getFollow());
                        channel1.setPrograms(channel.getPrograms());
                    });
        }

        return channel;
    }

    @Override
    public Optional<Channel> findById(Long id) {
        return channels.stream()
                .filter(channel -> channel.getId().equals(id))
                .map(channel -> channel.toBuilder().build())
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        channels.removeIf(channel -> channel.getId().equals(id) && (channel.getPrograms() == null || channel.getPrograms().size() == 0));
    }

    @Override
    public List<Channel> findAll() {
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
    public List<Channel> findChannelsByFollowEquals(boolean isFollow) {
        return channels.stream()
                .filter(channel -> channel.getFollow().equals(isFollow))
                .collect(Collectors.toList());
    }

    @Override
    public void setFollow(Long id, boolean follow) {
        channels.stream()
                .filter(channel -> channel.getId().equals(id))
                .forEach(channel -> channel.setFollow(follow));
    }
}
