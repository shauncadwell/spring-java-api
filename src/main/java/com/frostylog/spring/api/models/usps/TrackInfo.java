package com.frostylog.spring.api.models.usps;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackInfo {
    @JacksonXmlProperty(isAttribute = true)
    private String ID;

    @JacksonXmlProperty(localName = "TrackSummary")
    private String TrackSummary;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "TrackDetail")
    private List<String> TrackDetail = new ArrayList<>();

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getTrackSummary() {
        return TrackSummary;
    }

    public void setTrackSummary(String trackSummary) {
        TrackSummary = trackSummary;
    }

    public List<String> getTrackDetail() {
        return TrackDetail;
    }

    public void setTrackDetail(List<String> trackDetail) {
        TrackDetail = trackDetail;
    }

}
