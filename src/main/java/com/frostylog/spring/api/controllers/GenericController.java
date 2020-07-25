package com.frostylog.spring.api.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/v1/")
@RestController
public class GenericController {

    @CrossOrigin
    @RequestMapping(value = "/ups/{trackingNumber}", method = RequestMethod.GET)
    public String getUPS(@RequestParam final Optional<String> pParam, @PathVariable final String trackingNumber,
            @RequestHeader final Map<String, String> pHeaders) throws IOException, InterruptedException {

        HttpResponse<String> response;

        if (pHeaders.containsKey("accesslicensenumber")) {
            String strUrl = "https://onlinetools.ups.com/track/v1/details/" + trackingNumber + "?locale=en_US";
            System.out.println("Full URL = " + strUrl);
            String accessLicenseNumber = pHeaders.get("accesslicensenumber");

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(strUrl))
                    .setHeader("Content-Type", "application/json").setHeader("Accept", "*/*")
                    .setHeader("AccessLicenseNumber", accessLicenseNumber).build();
            response = client.send(request, BodyHandlers.ofString());
        } else {
            throw new RuntimeException("Header does not include AccessLicenseNumber");
        }

        return response.body();
    }

    @CrossOrigin
    @RequestMapping(value = "/ups/test", method = RequestMethod.GET)
    public String getTest() {

        return "Test complete";
    }

}