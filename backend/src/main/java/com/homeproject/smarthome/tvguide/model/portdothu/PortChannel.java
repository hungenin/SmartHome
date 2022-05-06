package com.homeproject.smarthome.tvguide.model.portdothu;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class PortChannel {
    private String id;
    private List<PortProgram> programs;
    private String article;
    private String name;
    private String domain;
    private String url;
    private String logo;
    private String stream_url;
    private String stream_ct_linkurl;
    private String banners;
    private String capture;
    private String date_from;
    private String date_until;
    private String cache;
}
