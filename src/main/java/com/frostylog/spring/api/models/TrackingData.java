package com.frostylog.spring.api.models;

import java.util.Optional;

import com.frostylog.spring.api.models.usps.TrackResponse;

public class TrackingData extends Object {
    private String trackingNumber;
    private String carrier;

    private Optional<TrackResponse> trackResponse;

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public TrackingData(String trackingNumber, String carrier) {
        this.trackingNumber = trackingNumber;
        this.carrier = carrier;
    }

    public TrackingData() {
    }

    public Optional<TrackResponse> getTrackResponse() {
        return trackResponse;
    }

    public void setTrackResponse(Optional<TrackResponse> trackResponse) {
        this.trackResponse = trackResponse;
    }
}