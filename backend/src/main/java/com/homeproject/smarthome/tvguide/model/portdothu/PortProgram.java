package com.homeproject.smarthome.tvguide.model.portdothu;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PortProgram {
    private String id;
    private String start_datetime;
    private String start_time;
    private Integer start_ts;
    private String end_time;
    private String end_datetime;
    private Boolean is_child_event;
    private String title;
    private String sound_quality;
    private Boolean italics;
    private String episode_title;
    private String description;
    private String short_description;
    private Boolean highlight;
    private Boolean is_repeat;
    private Boolean is_overlapping;
    private String film_id;
    private String film_url;
    private String favorite_url;
    private String del_calendar_url;
    private Boolean has_reminder;
    private Boolean show_reminder;
    private Boolean is_notified;
    private Boolean show_notification;
    private String media_url;
    private String media;
    private Boolean has_video;
    private String attributes_text;
    private OuterLinks outer_links;
    private Restriction restriction;
    private String type;
    private Boolean is_live;
}
