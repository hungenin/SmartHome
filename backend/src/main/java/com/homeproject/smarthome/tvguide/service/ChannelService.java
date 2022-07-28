package com.homeproject.smarthome.tvguide.service;

import com.homeproject.smarthome.tvguide.dao.ChannelDao;
import com.homeproject.smarthome.tvguide.exception.DataNotFoundException;
import com.homeproject.smarthome.tvguide.model.Channel;
import com.homeproject.smarthome.tvguide.model.dto.ChannelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private TvGuideService tvGuideService;

    public ChannelDto get(Short id) {
        return channelDao
                .findById(id)
                .map(ChannelDto::new)
                .orElseThrow(DataNotFoundException::new);
    }

    public ChannelDto update(Channel channel) {
        if (channelDao.existsById(channel.getId())) {
            return new ChannelDto(channelDao.save(channel));
        } else {
            throw new DataNotFoundException();
        }
    }

    public List<ChannelDto> channelsWithoutPrograms() throws RuntimeException{
        return tvGuideService
                .channelsWithoutPrograms()
                .stream()
                .map(ChannelDto::new)
                .collect(Collectors.toList());
    }

    public List<ChannelDto> followedChannelsWithPrograms() {
        return tvGuideService
                .followedChannelsWithPrograms()
                .stream()
                .map(ChannelDto::new)
                .collect(Collectors.toList());
    }

    public void updateChannelList() {
        tvGuideService.updateChannelListFromTvApiServer();
    }

    public void setAllFollow() {
        channelDao.setAllFollow(true);
    }
    public void setAllUnFollow() {
        channelDao.setAllFollow(false);
    }
}
