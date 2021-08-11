package com.homeproject.smarthome.tvGuide.service;

import com.homeproject.smarthome.tvGuide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvGuide.dao.ChannelRepository;
import com.homeproject.smarthome.tvGuide.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelService {
    @Autowired
    private ChannelRepository channelRepository;

    public Channel add(Channel channel) {
        channel.setId(null);
        return channelRepository.save(channel);
    }
    
    public Channel updateById(Long id, Channel channel) {
        if (channelRepository.existsById(id)) {
            channel.setId(id);
            return channelRepository.save(channel);
        } else {
            throw new DataNotFoundException();
        }
    }

    public Channel findById(Long id) {
        return channelRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }

    public List<Channel> findAll() {
        List<Channel> list = new ArrayList<>();
        channelRepository.findAll().forEach(list::add);
        return list;
    }

    public void deleteById(Long id) {
        if (channelRepository.existsById(id)) {
            channelRepository.deleteById(id);
        } else {
            throw new DataNotFoundException();
        }
    }
}
