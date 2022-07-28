package com.homeproject.smarthome.tvguide.datagrabber;

import com.homeproject.smarthome.tvguide.model.Channel;

import java.util.List;

public interface DataGrabber {
    List<Channel> getAvailableTvChannelsFromTvApiServer();
    List<Channel> refreshChannelsPrograms(List<Channel> channels);
    String getTvApiServerName();
}
