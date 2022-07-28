package com.homeproject.smarthome.tvguide.datagrabber.implemetation.portdothu;

import com.homeproject.smarthome.tvguide.datagrabber.DataGrabber;
import com.homeproject.smarthome.tvguide.exception.UnknownFormatException;
import com.homeproject.smarthome.tvguide.model.Channel;
import com.homeproject.smarthome.tvguide.model.Content;
import com.homeproject.smarthome.tvguide.model.portdothu.ElectronicProgrammingGuide;
import com.homeproject.smarthome.tvguide.model.Program;
import com.homeproject.smarthome.tvguide.model.portdothu.PortChannel;
import com.homeproject.smarthome.tvguide.model.portdothu.PortProgram;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Log4j2
@Service
public class DataGrabberPortDotHu implements DataGrabber {
    private final static String TV_API_SERVER_NAME = "port.hu";
    private final static int PORT_ID_MAX_VALUE = 500;
    private final static String BASE_URL = "https://port.hu/tvapi?";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Channel> getAvailableTvChannelsFromTvApiServer() {
        List<Channel> channels = new LinkedList<>();

        for (int i = 0; i < PORT_ID_MAX_VALUE; i++) {
            String tvApiURL = BASE_URL + "channel_id[]=tvchannel-" + i + "&date=" + LocalDate.now();

            try {
                final ElectronicProgrammingGuide epg = restTemplate.getForObject(tvApiURL, ElectronicProgrammingGuide.class);

                if (isEpgValid(epg)) {
                    channels.add(createChannelFromPortChannelBasicData(epg.getChannels().get(0)));
                }
            } catch (RuntimeException e) {
                log.error("{} - ({})", getErrorMessageFromRestTemplateException(e.getMessage()), tvApiURL);
            }
        }

        return channels;
    }

    @Override
    public List<Channel> refreshChannelsPrograms(List<Channel> channels) {
        if (!channels.isEmpty()) {
            try {
                final ElectronicProgrammingGuide epg = restTemplate.getForObject(generateTvApiURLFromChannels(channels), ElectronicProgrammingGuide.class);

                if (isEpgValid(epg)) {
                    return epg.getChannels().stream()
                            .map(portChannel -> createNewChannelFromPortChannelAndFromOriginalChannel(portChannel, channels))
                            .collect(Collectors.toList());
                } else {
                    log.error("Epg json object has probably changed. Cannot unpack to models.");
                }
            } catch (RuntimeException e) {
                log.error("{}", getErrorMessageFromRestTemplateException(e.getMessage()));
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String getTvApiServerName() {
        return TV_API_SERVER_NAME;
    }

    private static Channel createNewChannelFromPortChannelAndFromOriginalChannel(PortChannel portChannel, List<Channel> channels) {
        Channel channel = selectChannelFromListById(channels, getPortChannelId(portChannel));

        return new Channel(
                channel.getId(),
                channel.getName(),
                channel.getLogo(),
                channel.getFollow(),
                getProgramsFromPortChannel(portChannel, channel)
        );
    }

    private static List<Program> getProgramsFromPortChannel(PortChannel portChannel, Channel channel) {
        return portChannel
                .getPrograms()
                .stream()
                .map(portProgram -> convertPortProgramToProgramWithContent(portProgram, channel))
                .collect(Collectors.toList());
    }

    private static Program convertPortProgramToProgramWithContent(PortProgram portProgram, Channel channel) {
        Content content = new Content(null, portProgram.getTitle(), portProgram.getShort_description(), null);

        return new Program(
                null,
                getLocalDateTimeFromPortData(portProgram.getStart_datetime()),
                getLocalDateTimeFromPortData(portProgram.getEnd_datetime()),
                content,
                channel
        );
    }

    private static Channel selectChannelFromListById(List<Channel> channels, Short id) {
        return channels.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private static Channel createChannelFromPortChannelBasicData(PortChannel portChannel) {
        Channel channel = new Channel();

        channel.setId(getPortChannelId(portChannel));
        channel.setName(portChannel.getName());
        channel.setLogo(downloadLogoToBase64String(portChannel.getLogo()));

        return channel;
    }

    private static short getPortChannelId(PortChannel portChannel) {
        return Short.parseShort(portChannel.getId().substring(10));
    }

    private static List<Short> getChannelIdList(List<Channel> channels) {
        return channels.stream()
                .map(Channel::getId)
                .collect(Collectors.toList());
    }

    private static String generateTvApiURLFromChannels(List<Channel> channels) {
        StringBuilder tvApiUrl = new StringBuilder(BASE_URL);

        for (Short id : getChannelIdList(channels)) {
            tvApiUrl.append("channel_id[]=tvchannel-").append(id).append("&");
        }

        appendTimeToday(tvApiUrl);

        return tvApiUrl.toString();
    }

    private static void appendTimeToday(StringBuilder tvApiUrl) {
        tvApiUrl.append("date=").append(LocalDate.now());
    }

    private static void appendTimeInterval(StringBuilder tvApiUrl, byte minusDays, byte plusDays) {
        tvApiUrl
                .append("&i_datetime_from=")
                .append(LocalDate.now().minusDays(minusDays))
                .append("&i_datetime_to=")
                .append(LocalDate.now().plusDays(plusDays + 1));
    }

    private static LocalDateTime getLocalDateTimeFromPortData(String dateTime) {
        return LocalDateTime.of(
                Integer.parseInt(dateTime.split("T")[0].split("-")[0]),
                Integer.parseInt(dateTime.split("T")[0].split("-")[1]),
                Integer.parseInt(dateTime.split("T")[0].split("-")[2]),
                Integer.parseInt(dateTime.split("T")[1].split(":")[0]),
                Integer.parseInt(dateTime.split("T")[1].split(":")[1])
        );
    }

    private static String downloadLogoToBase64String(String logoURL) {
        if (isBase64String(logoURL)) {
            return logoURL;
        }

        try {
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
        } catch (UnknownFormatException e) {
            log.error("Can't download logo from port.hu! Unknown image format. ( {} )", logoURL);
        } catch (IOException e) {
            log.error("Can't download logo from port.hu! Message: {} ( {} )", e.getMessage(), logoURL);
        }

        return "";
    }

    private static String generateBase64PreStringFromURL(String logoURL) {
        if (logoURL.endsWith("jpg") || logoURL.endsWith("jpeg")) {
            return "data:image/jpeg;base64,";
        }
        if (logoURL.endsWith("png")) {
            return  "data:image/png;base64,";
        }
        if (logoURL.endsWith("bmp")) {
            return  "data:image/bmp;base64,";
        }

        throw new UnknownFormatException();
    }

    private static boolean isBase64String(String logoURL) {
        return logoURL.startsWith("data");
    }

    private static boolean isEpgValid(ElectronicProgrammingGuide epg) {
        return epg != null && epg.getChannels() != null && !epg.getChannels().isEmpty();
    }

    private static String getErrorMessageFromRestTemplateException(String errorMessage) {
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
