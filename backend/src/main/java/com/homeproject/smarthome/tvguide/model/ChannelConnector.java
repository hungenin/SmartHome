package com.homeproject.smarthome.tvguide.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity(name = "channelConnector")
public class ChannelConnector {
    @Id
    @NotNull
    long channelId;
    @NotNull
    int portId;
}
