package com.homeproject.smarthome.tvguide.model.portdothu;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class Epg {
    private String date;
    private String date_from;
    private String date_to;
    private List<PortChannel> channels;
    private EveningStartTime eveningStartTime;
}
