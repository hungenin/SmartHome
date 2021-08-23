package com.homeproject.smarthome.tvGuide.configuration;

import com.homeproject.smarthome.tvGuide.dao.ChannelDao;
import com.homeproject.smarthome.tvGuide.dao.ContentDao;
import com.homeproject.smarthome.tvGuide.dao.ProgramDao;
import com.homeproject.smarthome.tvGuide.dao.implementation.database.ChannelDaoRepository;
import com.homeproject.smarthome.tvGuide.dao.implementation.database.ContentDaoRepository;
import com.homeproject.smarthome.tvGuide.dao.implementation.database.ProgramDaoRepository;
import com.homeproject.smarthome.tvGuide.dao.implementation.memory.ChannelDaoMemory;
import com.homeproject.smarthome.tvGuide.dao.implementation.memory.ContentDaoMemory;
import com.homeproject.smarthome.tvGuide.dao.implementation.memory.ProgramDaoMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DaoConfiguration {
    private String daoType = "jpa";
    @Autowired
    private ChannelDaoRepository channelDaoRepository;
    @Autowired
    private ContentDaoRepository contentDaoRepository;
    @Autowired
    private ProgramDaoRepository programDaoRepository;

    @Bean
    @Primary
    public ChannelDao getChannelDao() {
        switch (daoType) {
            case "jpa":
                return channelDaoRepository;
            default:
                return new ChannelDaoMemory();
        }
    }

    @Bean
    @Primary
    public ContentDao getContentDao() {
        switch (daoType) {
            case "jpa":
                return contentDaoRepository;
            default:
                return new ContentDaoMemory();
        }
    }

    @Bean
    @Primary
    public ProgramDao getProgramDao() {
        switch (daoType) {
            case "jpa":
                return programDaoRepository;
            default:
                return new ProgramDaoMemory();
        }
    }
}