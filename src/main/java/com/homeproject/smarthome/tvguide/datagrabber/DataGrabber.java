package com.homeproject.smarthome.tvguide.datagrabber;

import com.homeproject.smarthome.tvguide.model.Channel;

public interface DataGrabber {
    void updateChannelList();
    void refreshChannels();
    void refreshChannel(Channel channel);
}
