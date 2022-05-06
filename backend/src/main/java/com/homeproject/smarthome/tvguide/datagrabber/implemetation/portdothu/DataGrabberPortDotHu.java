package com.homeproject.smarthome.tvguide.datagrabber.implemetation.portdothu;

import com.homeproject.smarthome.tvguide.dao.ChannelConnectorDao;
import com.homeproject.smarthome.tvguide.dao.ChannelDao;
import com.homeproject.smarthome.tvguide.dao.ContentDao;
import com.homeproject.smarthome.tvguide.dao.ProgramDao;
import com.homeproject.smarthome.tvguide.datagrabber.DataGrabber;
import com.homeproject.smarthome.tvguide.model.Channel;
import com.homeproject.smarthome.tvguide.model.ChannelConnector;
import com.homeproject.smarthome.tvguide.model.Content;
import com.homeproject.smarthome.tvguide.model.portdothu.Epg;
import com.homeproject.smarthome.tvguide.model.Program;
import com.homeproject.smarthome.tvguide.model.portdothu.PortChannel;
import com.homeproject.smarthome.tvguide.model.portdothu.PortProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class DataGrabberPortDotHu implements DataGrabber {
    private final static int PORT_ID_MAX_VALUE = 500;
    private final static String BASE_URL = "https://port.hu/tvapi?";
    private final static String LOG_MESSAGE_BASE = " :: Data grabber (port.hu) - ";

    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private ContentDao contentDao;
    @Autowired
    private ProgramDao programDao;
    @Autowired
    private ChannelConnectorDao channelConnectorDao;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void updateChannelList() {
        System.err.println(LocalDateTime.now() + LOG_MESSAGE_BASE + "updating TV channels...");

        List<PortChannel> portChannels = discoverAvailableTvChannelsFromPortDotHu();
        updateExistedTvChannelsDataFromPortDotHuChannels(portChannels);

        System.err.println(LocalDateTime.now() + LOG_MESSAGE_BASE + "TV channels up to date");
    }

    @Override
    public void refreshChannels() {
        String tvApiURL = generateTvApiURLByFollowedChannels();

        final Epg epgData = restTemplate.getForObject(tvApiURL, Epg.class);
        if (epgData != null && epgData.getChannels() != null && !epgData.getChannels().isEmpty()) {
            epgData.getChannels().forEach(portChannel -> {
                int portId = Integer.parseInt(portChannel.getId().substring(10));

                channelConnectorDao
                        .findByPortId(portId)
                        .flatMap(channelConnector -> channelDao.findById(channelConnector.getChannelId()))
                        .ifPresent(channel -> {
                            deleteOldProgramsFromChannel(channel);
                            createAndSaveProgramsToChannelFromPortChannels(channel, portChannel);
                        });
                
                System.err.println(portChannel.getName() + " refreshed");
            });
        }
    }

    @Override
    public void refreshChannel(Channel channel) {
        Optional<ChannelConnector> channelConnector = channelConnectorDao.findByChannelId(channel.getId());
        if (channelConnector.isPresent()) {
            String tvApiURL = generateTvApiURLByPortId(channelConnector.get().getPortId());

            final Epg epgData = restTemplate.getForObject(tvApiURL, Epg.class);
            if (epgData != null && epgData.getChannels() != null && !epgData.getChannels().isEmpty()) {
                deleteOldProgramsFromChannel(channel);
                createAndSaveProgramsToChannelFromPortChannels(channel, epgData.getChannels().get(0));

                System.err.println(epgData.getChannels().get(0).getName() + " refreshed");
            }
        }
    }
    
    private void deleteOldProgramsFromChannel(Channel channel) {
        channel.getPrograms().forEach(program -> {
            Long contentID = program.getContent().getId();
            programDao.deleteById(program.getId());
            contentDao.deleteById(contentID);
        });
    }

    private void createAndSaveProgramsToChannelFromPortChannels(Channel channel, PortChannel portChannel) {
        List<PortProgram> portPortPrograms = portChannel.getPrograms();
        portPortPrograms.forEach(portPortProgram -> {
            Content content = new Content(null, portPortProgram.getTitle(), portPortProgram.getShort_description(), null);
            contentDao.save(content);

            Program program = new Program(null, getLocalDateTimeFromPortData(portPortProgram.getStart_datetime()), getLocalDateTimeFromPortData(portPortProgram.getEnd_datetime()), content, channel);
            programDao.save(program);


            channel.addProgram(program);
        });
    }

    private List<PortChannel> discoverAvailableTvChannelsFromPortDotHu() {
        List<PortChannel> portChannels = new LinkedList<>();

        for (int i = 0; i < PORT_ID_MAX_VALUE; i++) {
            String tvApiURL = BASE_URL + "channel_id[]=tvchannel-" + i + "&date=" + LocalDate.now();

            try {
                final Epg epgData = restTemplate.getForObject(tvApiURL, Epg.class);

                if (epgData == null || epgData.getChannels() == null) {
                    System.err.println(LocalDateTime.now() + LOG_MESSAGE_BASE + "json object has probably changed. Cannot unpack to models. ( " + tvApiURL + " )");
                } else {
                    if (!epgData.getChannels().isEmpty()) {
                        portChannels.add(epgData.getChannels().get(0));
                    }
                }
            } catch (RuntimeException e) {
                System.err.println(LocalDateTime.now() + LOG_MESSAGE_BASE + getErrorMessageFromRestTemplateException(e.getMessage()) + " - ( " + tvApiURL + " )");
            }
        }

        return portChannels;
    }

    private void updateExistedTvChannelsDataFromPortDotHuChannels(List<PortChannel> portChannels) {
        List<Channel> channels = channelDao.findAll();

        for (Channel channel : channels) {
            ChannelConnector channelConnector = channelConnectorDao.findByChannelId(channel.getId()).orElse(null);

            if (channelConnector != null) {
                boolean isChannelRemoved = true;

                for (int i = 0; i < portChannels.size(); ) {
                    PortChannel portChannel = portChannels.get(i);

                    int portId = Integer.parseInt(portChannel.getId().substring(10));

                    if (portId == channelConnector.getPortId()) {
                        isChannelRemoved = false;

                        updateChannelData(channel, portChannel);
                        portChannels.remove(portChannel);
                    } else {
                        i++;
                    }
                }

                if (isChannelRemoved) {
                    channelDao.deleteById(channel.getId());
                    channelConnectorDao.deleteByChannelId(channel.getId());

                    System.err.println(LocalDateTime.now() + LOG_MESSAGE_BASE + "channel has been removed from port.hu ( " + channel.getName() + " )");
                }
            } else {
                channelDao.deleteById(channel.getId());
                System.err.println(LocalDateTime.now() + LOG_MESSAGE_BASE + "channel connection not found. Delete channel ( " + channel.getName() + " )");
            }
        }

        addNewPortDotHuChannelsToChannelList(portChannels);
    }

    private void addNewPortDotHuChannelsToChannelList(List<PortChannel> portChannels) {
        for (PortChannel portChannel : portChannels) {
            createNewChannelAndConnectToPortId(portChannel);
        }
    }

    private void updateChannelData(Channel channel, PortChannel portChannel) {
        boolean isUpdated = false;

        if (!channel.getName().equals(portChannel.getName())) {
            isUpdated = true;

            System.err.println(LocalDateTime.now() + LOG_MESSAGE_BASE + "channel name updated ( " + channel.getName() + " -> " + portChannel.getName() + " )");
            channel.setName(portChannel.getName());
        }

        String logo = downloadLogoToBase64String(portChannel.getLogo());
        if (!channel.getLogo().equals(logo)) {
            isUpdated = true;

            System.err.println(LocalDateTime.now() + LOG_MESSAGE_BASE + "channel logo updated ( " + channel.getName() + " )");
            channel.setLogo(logo);
        }

        if (isUpdated) {
            channelDao.save(channel);
        }
    }

    private String downloadLogoToBase64String(String logoURL) {
        try {
            if (logoURL.startsWith("data")) {
                return logoURL;
            }

            URL url = new URL(logoURL);

            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int n;
            byte[] buffer = new byte[1024];
            while (-1 != (n = in.read(buffer))) {
                out.write(buffer, 0, n);
            }
            out.close();
            in.close();

            return generateBase64PreStringFromURL(logoURL) + Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String generateBase64PreStringFromURL(String logoURL) {
        if (logoURL.endsWith("jpg") || logoURL.endsWith("jpeg")) {
            return "data:image/jpeg;base64,";
        }
        if (logoURL.endsWith("png")) {
            return  "data:image/png;base64,";
        }
        if (logoURL.endsWith("bmp")) {
            return  "data:image/bmp;base64,";
        }

        System.err.println(LocalDateTime.now() + LOG_MESSAGE_BASE + "error while downloading logo from port.hu! Unknown image format. ( " + logoURL + " )");
        return "";
    }

    private void createNewChannelAndConnectToPortId(PortChannel portChannel) {
        Channel channel = new Channel();
        channel.setName(portChannel.getName());
        channel.setLogo(downloadLogoToBase64String(portChannel.getLogo()));
        channel.setFollow(true);

        channelDao.save(channel);

        ChannelConnector channelConnector = new ChannelConnector();
        channelConnector.setChannelId(channel.getId());
        channelConnector.setPortId(Integer.parseInt(portChannel.getId().substring(10)));

        channelConnectorDao.save(channelConnector);

        System.err.println(LocalDateTime.now() + LOG_MESSAGE_BASE + "new channel added ( " + channel.getName() + " )");
    }

    private String generateTvApiURLByFollowedChannels() {
        List<Integer> portIds = getFollowedChannelsPortIds();

        StringBuilder tvApiUrl = new StringBuilder(BASE_URL);
        for (Integer id : portIds) {
            if (id != null) {
                tvApiUrl.append("channel_id[]=tvchannel-").append(id).append("&");
            }
        }
        appendTimeInterval(tvApiUrl);

        return tvApiUrl.toString();
    }

    private List<Integer> getFollowedChannelsPortIds() {
        return channelDao.findChannelsByFollowEquals(true).stream()
                .map(this::getPortIdByChannel)
                .collect(Collectors.toList());
    }

    private Integer getPortIdByChannel(Channel channel) {
        ChannelConnector channelConnector = channelConnectorDao.findByChannelId(channel.getId()).orElse(null);
        if (channelConnector != null) {
            return channelConnector.getPortId();
        }

        return null;
    }

    private String generateTvApiURLByPortId(Integer portId) {
        StringBuilder tvApiUrl = new StringBuilder(BASE_URL);
        if (portId != null) {
            tvApiUrl.append("channel_id[]=tvchannel-").append(portId).append("&");
        }
        appendTimeInterval(tvApiUrl);

        return tvApiUrl.toString();
    }

    private void appendTimeInterval(StringBuilder tvApiUrl) {
        // TODO: idő intervallumot megadni
        tvApiUrl.append("date=").append(LocalDate.now());
        /*tvApiUrl
                .append("i_datetime_from=")
                .append(LocalDate.now().minusDays(1))
                .append("&i_datetime_to=")
                .append(LocalDate.now().plusDays(2));*/
    }

    private LocalDateTime getLocalDateTimeFromPortData(String dateTime) {
        return LocalDateTime.of(
                Integer.parseInt(dateTime.split("T")[0].split("-")[0]),
                Integer.parseInt(dateTime.split("T")[0].split("-")[1]),
                Integer.parseInt(dateTime.split("T")[0].split("-")[2]),
                Integer.parseInt(dateTime.split("T")[1].split(":")[0]),
                Integer.parseInt(dateTime.split("T")[1].split(":")[1])
        );
    }

    private String getErrorMessageFromRestTemplateException(String errorMessage) {
        try {
            String status = errorMessage.split("status\":")[1].split("}")[0];
            String name = errorMessage.split("name\":\"")[1].split("\",\"")[0];
            String message = errorMessage.split("message\":\"")[1].split("\",\"")[0];
            return status + " " + name + " ( " + message + " )";
        } catch (RuntimeException e) {
            return errorMessage;
        }
    }
}
