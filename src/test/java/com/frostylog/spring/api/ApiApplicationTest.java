package com.frostylog.spring.api;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.frostylog.spring.api.models.usps.TrackResponse;

import org.junit.Ignore;
import org.junit.Test;

public class ApiApplicationTest {

    @Ignore
    @Test
    public void testXML() throws IOException {
        String potentialResponse = "<TrackResponse>" + "<TrackInfo ID=\"9300120111404953153460\">"
                + "<trackSummary>Your item was delivered in or at the mailbox at 12:41 pm on June 26, 2020 in BONAIRE, GA 31005.</TrackSummary>"
                + "<trackDetail>Out for Delivery, 06/26/2020, 8:55 am, BONAIRE, GA 31005</TrackDetail>"
                + "<trackDetail>Arrived at Post Office, June 26, 2020, 8:44 am, BONAIRE, GA 31005</TrackDetail>"
                + "<trackDetail>Departed USPS Regional Facility, 06/26/2020, 5:51 am, MACON GA DISTRIBUTION CENTER ANNEX</TrackDetail>"
                + "<trackDetail>Arrived at USPS Regional Facility, June 25, 2020, 11:07 pm, MACON GA DISTRIBUTION CENTER ANNEX</TrackDetail>"
                + "<trackDetail>Departed USPS Regional Facility, June 25, 2020, 5:48 pm, MACON GA DISTRIBUTION CENTER</TrackDetail>"
                + "<trackDetail>Arrived at USPS Regional Facility, June 25, 2020, 2:42 am, MACON GA DISTRIBUTION CENTER</TrackDetail>"
                + "<trackDetail>Departed USPS Regional Facility, June 24, 2020, 11:41 pm, ATLANTA GA NETWORK DISTRIBUTION CENTER</TrackDetail>"
                + "<trackDetail>Arrived at USPS Facility, June 24, 2020, 7:58 pm, ATLANTA, GA 30354</TrackDetail>"
                + "<trackDetail>Departed USPS Regional Facility, 06/24/2020, 7:15 am, AKRON OH DISTRIBUTION CENTER</TrackDetail>"
                + "<trackDetail>Arrived at USPS Regional Origin Facility, 06/24/2020, 12:56 am, AKRON OH DISTRIBUTION CENTER</TrackDetail>"
                + "<trackDetail>USPS in possession of item, June 23, 2020, 8:21 am, MENTOR, OH 44061</TrackDetail>"
                + "</TrackInfo>" + "</TrackResponse>";

        XmlMapper xmlMapper = new XmlMapper();
        System.out.println("blah");
        TrackResponse response = xmlMapper.readValue(potentialResponse, TrackResponse.class);
        xmlMapper.writeValue(new File("trackResponse.xml"), new TrackResponse());

        System.out.println("blah");
    }

}