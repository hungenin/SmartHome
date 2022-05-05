package com.homeproject.smarthome.tvguide.service;

import com.homeproject.smarthome.tvguide.dao.ChannelDao;
import com.homeproject.smarthome.tvguide.datagrabber.DataGrabber;
import com.homeproject.smarthome.tvguide.model.Channel;
import com.homeproject.smarthome.tvguide.model.Program;
import com.homeproject.smarthome.tvguide.model.dto.ChannelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TvGuideService {
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private DataGrabber dataGrabber;

    public List<ChannelDto> followedChannelsWithPrograms() {
        List<Channel> channels = channelDao.findChannelsByFollowEquals(true);
        channels.forEach(this::refreshProgramsIfExpired);

        return channels.stream()
                .map(ChannelDto::new)
                .collect(Collectors.toList());
    }

    private void refreshProgramsIfExpired(Channel channel) {
        if (channel.getPrograms().isEmpty() || getLastProgram(channel).getEnd().isBefore(LocalDateTime.now())) {
            dataGrabber.refreshChannel(channel);
        }
    }

    private Program getLastProgram(Channel channel) {
        return channel.getPrograms().get(channel.getPrograms().size() - 1);
    }
}
