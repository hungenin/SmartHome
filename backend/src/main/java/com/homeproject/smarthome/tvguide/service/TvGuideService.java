package com.homeproject.smarthome.tvguide.service;

import com.homeproject.smarthome.tvguide.dao.ChannelDao;
import com.homeproject.smarthome.tvguide.dao.ContentDao;
import com.homeproject.smarthome.tvguide.dao.ProgramDao;
import com.homeproject.smarthome.tvguide.datagrabber.DataGrabber;
import com.homeproject.smarthome.tvguide.exception.CannotBeReadException;
import com.homeproject.smarthome.tvguide.model.Channel;
import com.homeproject.smarthome.tvguide.model.Program;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class TvGuideService {
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private ProgramDao programDao;
    @Autowired
    private ContentDao contentDao;
    @Autowired
    private DataGrabber dataGrabber;

    public List<Channel> channelsWithoutPrograms() {
        List<Channel> channels;
        try {
            channels = channelDao.findAll();
        } catch (RuntimeException e) {
            log.error("Can't read TV channel list from channel DAO! Error message: {}", e.getMessage());

            throw new CannotBeReadException(String.format("Can't read TV channel list from channel DAO! Error message: %s", e.getMessage()));
        }

        return removePrograms(channels.isEmpty() ? updateChannelListFromTvApiServer() : channels);
    }

    public List<Channel> followedChannelsWithPrograms() {
        try {
            List<Channel> followedChannels = channelDao.findChannelsByFollowEquals(true);

            List<Channel> channelsToUpdate = followedChannels
                    .stream()
                    .filter(TvGuideService::isProgramsExpired)
                    .collect(Collectors.toList());

            try {
                List<Channel> channelsUpdated = dataGrabber.refreshChannelsPrograms(channelsToUpdate);
                for (Channel channel : channelsUpdated) {
                    deleteProgramsFromChannel(getChannelByIdFromList(channelsToUpdate, channel.getId()));
                    createAndSaveProgramsFromChannel(channel);
                    log.info("Tv channel's program ( {} ) refreshed.", channel.getName());
                }

                return followedChannels
                        .stream()
                        .map(channel -> getUpdatedChannelFromListIfExistsOrGetOriginalChannel(channel, channelsUpdated))
                        .collect(Collectors.toList());
            } catch (RuntimeException e) {
                log.error("Can't update channel's programs! Message: {}", e.getMessage());
                return followedChannels;
            }
        } catch (RuntimeException e) {
            log.error("Can't read followed TV channel from channel DAO! Error message: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Channel> updateChannelListFromTvApiServer() {
        log.info("Start updating TV channel list from TV API server. ( {} )", dataGrabber.getTvApiServerName());

        try {
            List<Channel> tvApiServerChannels = dataGrabber.getAvailableTvChannelsFromTvApiServer();
            try {
                List<Channel> updatedChannels = updateExistedTvChannelsDataFromTvApiServerChannels(channelDao.findAll(), tvApiServerChannels);
                log.info("TV channel list is up to date. ( {} )", dataGrabber.getTvApiServerName());

                return updatedChannels;
            } catch (RuntimeException e) {
                log.error("Can't read TV channel list from channel DAO! Error message: {}", e.getMessage());
            }
        } catch (RuntimeException e) {
            log.error("Can't update TV channel list from TV API server. ( {} ) Error message: {}", dataGrabber.getTvApiServerName(), e.getMessage());
        }

        return new ArrayList<>();
    }

    private List<Channel> updateExistedTvChannelsDataFromTvApiServerChannels(List<Channel> channels, List<Channel> tvApiServerChannels) {
        List<Channel> updatedChannels = new ArrayList<>();

        for (Channel channel : channels) {
            boolean isChannelRemoved = true;

            for (int i = 0; i < tvApiServerChannels.size(); ) {
                Channel tvApiServerChannel = tvApiServerChannels.get(i);

                if (tvApiServerChannel.getId().equals(channel.getId())) {
                    isChannelRemoved = false;

                    updateChannelData(channel, tvApiServerChannel);
                    tvApiServerChannels.remove(tvApiServerChannel);
                    updatedChannels.add(channel);
                } else {
                    i++;
                }
            }

            if (isChannelRemoved) {
                try {
                    channelDao.deleteById(channel.getId());
                    log.info("TV channel has been removed from TV API server. ( {} ) Delete TV channel. ( {} )", dataGrabber.getTvApiServerName(), channel.getName());
                } catch (RuntimeException e) {
                    log.error("Can't delete removed TV channel! ( {} ) Error message: {}", channel.getName(), e.getMessage());
                }
            }
        }

        addNewTvApiServerChannelsToChannelList(tvApiServerChannels);
        updatedChannels.addAll(tvApiServerChannels);

        return updatedChannels;
    }

    private void updateChannelData(Channel channelWithOldData, Channel channelWithUpdatedData) {
        byte isUpdated = 0;
        if (!channelWithOldData.getName().equals(channelWithUpdatedData.getName())) {
            isUpdated++;
            channelWithOldData.setName(channelWithUpdatedData.getName());
        }
        if (!channelWithOldData.getLogo().equals(channelWithUpdatedData.getLogo())) {
            isUpdated += 2;
            channelWithOldData.setLogo(channelWithUpdatedData.getLogo());
        }

        if (isUpdated > 0) {
            try {
                channelDao.save(channelWithOldData);
                switch (isUpdated) {
                    case 1:
                        log.info("TV channel name updated ( {} -> {} )", channelWithOldData.getName(), channelWithUpdatedData.getName());
                        break;
                    case 2:
                        log.info("TV channel logo updated ( {} )", channelWithOldData.getName());
                        break;
                    default:
                        log.info("TV channel name updated ( {} -> {} )", channelWithOldData.getName(), channelWithUpdatedData.getName());
                        log.info("TV channel logo updated ( {} )", channelWithOldData.getName());
                        break;
                }
            } catch (RuntimeException e) {
                log.error("Can't update TV channel ( {} ) to DAO! Error message: {}", channelWithOldData.getName(), e.getMessage());
            }
        }
    }

    private void addNewTvApiServerChannelsToChannelList(List<Channel> newTvApiServerChannels) {
        for (Channel newTvApiServerChannel : newTvApiServerChannels) {
            newTvApiServerChannel.setFollow(false);

            try {
                channelDao.save(newTvApiServerChannel);
                log.info("New TV channel added. ( {} )", newTvApiServerChannel.getName());
            } catch (RuntimeException e) {
                log.error("Can't add new TV channel ( {} ) to DAO! Error message: {}", newTvApiServerChannel.getName(), e.getMessage());
            }
        }
    }

    private void deleteProgramsFromChannel(Channel channel) {
        if (channel != null && channel.getPrograms() != null) {
            channel.getPrograms().forEach(program -> {
                if (program.getId() != null) {
                    try {
                        programDao.deleteById(program.getId());
                    } catch (RuntimeException e) {
                        log.error("Can't delete TV program ( {} ) from DAO! Error message: {}", program.getId(), e.getMessage());
                    }
                }
                if (program.getContent() != null && program.getContent().getId() != null) {
                    try {
                        contentDao.deleteById(program.getContent().getId());
                    } catch (RuntimeException e) {
                        log.error("Can't delete TV program's content ( {} ) from DAO! Error message: {}", program.getContent().getId(), e.getMessage());
                    }
                }
            });
        }
    }

    private void createAndSaveProgramsFromChannel(Channel channel) {
        channel.getPrograms().forEach(program -> {
            try {
                contentDao.save(program.getContent());
                try {
                    programDao.save(program);
                } catch (RuntimeException e) {
                    log.error("Can't save TV program ( {} ) to DAO! Error message: {}", program.getId(), e.getMessage());
                }
            } catch (RuntimeException e) {
                log.error("Can't save TV program's content ( {} )  and TV program ( {} ) to DAO! Error message: {}", program.getContent().getId(), program.getId(), e.getMessage());
            }
        });
    }

    private static List<Channel> removePrograms(List<Channel> channels) {
        channels.forEach(channel -> channel.setPrograms(new ArrayList<>()));

        return channels;
    }

    private static Channel getUpdatedChannelFromListIfExistsOrGetOriginalChannel(Channel channel, List<Channel> channels) {
        return channels
                .stream()
                .filter(updatedChannel -> updatedChannel.getId() != null && updatedChannel.getId().equals(channel.getId()))
                .findFirst()
                .orElse(channel);
    }

    private static boolean isProgramsExpired(Channel channel) {
        return channel.getPrograms().isEmpty() || getLastProgram(channel).getEnd().isBefore(LocalDateTime.now());
    }

    private static Program getLastProgram(Channel channel) {
        return channel.getPrograms().get(channel.getPrograms().size() - 1);
    }

    public static Channel getChannelByIdFromList(List<Channel> channels, Short id) {
        return channels.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
