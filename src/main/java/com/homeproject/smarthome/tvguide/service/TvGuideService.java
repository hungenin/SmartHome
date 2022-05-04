package com.homeproject.smarthome.tvGuide.service;

import com.homeproject.smarthome.tvGuide.dao.ChannelDao;
import com.homeproject.smarthome.tvGuide.dao.ContentDao;
import com.homeproject.smarthome.tvGuide.dao.ProgramDao;
import com.homeproject.smarthome.tvGuide.data_sample.ChannelCreator;
import com.homeproject.smarthome.tvGuide.data_sample.ContentCreator;
import com.homeproject.smarthome.tvGuide.data_sample.ProgramCreator;
import com.homeproject.smarthome.tvGuide.model.Channel;
import com.homeproject.smarthome.tvGuide.model.dto.ChannelDto;
import com.homeproject.smarthome.tvGuide.model.dto.ProgramDto;
import com.homeproject.smarthome.tvGuide.model.portdothu.Epg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TvGuideService {
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private ProgramDao programDao;
    @Autowired
    private ContentDao contentDao;
    @Autowired
    private RestTemplate restTemplate;

    public List<ChannelDto> followedChannelsWithPrograms() {
        return channelDao.findChannelsByFollowEquals(true).stream()
                .map(channel -> ChannelDto.builder()
                        .id(channel.getId())
                        .name(channel.getName())
                        .follow(channel.getFollow())
                        .programs(channel.getPrograms()
                                .stream()
                                .map(ProgramDto::new)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    public void init() {
        ContentCreator.getContents().forEach(contentDao::save);
        ChannelCreator.getChannels().forEach(channelDao::save);
        ProgramCreator.getPrograms(contentDao.findAll(), channelDao.findAll()).forEach(programDao::save);
    }
    
    public void updateDataFromPortDotHu() {
        List<Integer> portIds = Arrays.asList(1, 2, 3, 4, 5, 6, 8, 9, 10, 14, 15, 16, 17, 19, 20, 21, 23, 32, 35, 37, 38, 41, 42, 44, 46, 47, 59, 60, 64, 65, 66, 68, 71, 77, 79, 80, 82, 83, 84, 85, 89, 90, 91, 94, 95, 96, 98, 99, 103, 114, 115, 126, 132, 134, 138, 139, 141, 143, 144, 146, 147, 149, 150, 153, 156, 158, 159, 164, 170, 173, 176, 177, 178, 179, 180, 182, 188, 189, 190, 194, 196, 197, 202, 207, 211, 212, 213, 215, 216, 217, 218, 222, 223, 224, 226, 227, 228, 229, 230, 231, 233, 235, 236, 240, 241, 244, 245, 246, 247, 248, 249, 250, 251, 253, 257, 265, 266, 275, 278, 279, 282, 284, 285, 290, 294, 295, 297, 298, 300, 301, 303, 304, 305, 307, 309, 310, 311, 313, 316, 325, 359, 362, 363, 364, 365, 366, 367, 368, 370, 371, 372, 373, 374);


        StringBuilder tvApiUrl = new StringBuilder();
        tvApiUrl.append("https://port.hu/tvapi?");
        for (Integer id : portIds) {
            tvApiUrl.append("channel_id[]=tvchannel-" + id + "&");
        }
        tvApiUrl.append("date=" + LocalDate.now());
        
        final Epg epgData = restTemplate.getForObject(tvApiUrl.toString(), Epg.class);
        epgData.getChannels().forEach(channel -> {
            Channel channel1;
            int port = Integer.parseInt(channel.getId().substring(10));
            System.out.println(port);
            if (!channelDao.existsChannelByPortIdEquals(port)) {
                channel1 = new Channel();
                channel1.setPortId(port);
                channel1.setFollow(false);
            } else {
                channel1 = channelDao.findChannelsByPortIdEquals(Integer.valueOf(channel.getId().substring(10)));
            }
            channel1.setName(channel.getName());

            channelDao.save(channel1);
        });
    }
}
