package com.frostylog.spring.api.models.usps;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackResponse {
    @JacksonXmlProperty(localName = "TrackInfo")
    private TrackInfo trackInfo;

    Optional<String> error;

    public TrackInfo getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }

    public Optional<String> getError() {
        return error;
    }

    public void setError(Optional<String> error) {
        this.error = error;
    }

}