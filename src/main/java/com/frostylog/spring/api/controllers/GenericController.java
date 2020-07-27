package com.frostylog.spring.api.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.frostylog.spring.api.models.TrackingData;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.net.SyslogOutputStream;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@RequestMapping(value = "/api/v1/")
@RestController
public class GenericController {

    @CrossOrigin
    @RequestMapping(value = "/ups/{pTrackingNumber}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public Map<Object, Object> getOneFromUPS(@RequestParam final Optional<String> pParam,
            @PathVariable final String pTrackingNumber, @RequestHeader final Map<String, String> pHeaders)
            throws IOException, InterruptedException, ParseException {

        return getResponseFromUPS(pTrackingNumber, pParam, pHeaders);
    }

    @CrossOrigin
    @RequestMapping(value = "/ups", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public List<Object> getManyFromUPS(@RequestBody final List<String> pTrackingNumbers,
            @RequestParam final Optional<String> pParam, @RequestHeader final Map<String, String> pHeaders)
            throws IOException, InterruptedException, ParseException {
        List<Object> responseList = new ArrayList<>();
        for (String trackingNumber : pTrackingNumbers) {
            responseList.add(getResponseFromUPS(trackingNumber, pParam, pHeaders));
        }
        return responseList;
    }

    @CrossOrigin
    @RequestMapping(value = "/tracking", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public List<Object> getTrackingData(@RequestBody final List<TrackingData> pTrackingNumbers,
            @RequestParam final Optional<String> pParam, @RequestHeader final Map<String, String> pHeaders)
            throws IOException, InterruptedException, ParseException {
        List<Object> responseList = new ArrayList<>();
        for (TrackingData data : pTrackingNumbers) {
            switch (data.getCarrier().toLowerCase()) {
                case "ups":
                    responseList.add(getResponseFromUPS(data.getTrackingNumber(), pParam, pHeaders));
                    break;
                case "usps":
                    responseList.add(getResponseFromUSPS(data.getTrackingNumber(), pParam, pHeaders));
                    break;
                case "dhl":
                    responseList.add(getResponseFromDHL(data.getTrackingNumber(), pParam, pHeaders));
                    break;
                case "fedex":
                    responseList.add(getResponseFromFEDEX(data.getTrackingNumber(), pParam, pHeaders));
                    break;
                default:
                    Map<Object, Object> responseBuilder = new HashMap<>();
                    responseBuilder.put("trackingNumber", data.getTrackingNumber());
                    responseBuilder.put("Error: ", data.getCarrier().toLowerCase() + " is not a valid carrier.");
                    responseList.add(responseBuilder);

                    break;
            }
        }
        return responseList;
    }

    private Object getResponseFromFEDEX(String pTrackingNumber, Optional<String> pParam, Map<String, String> pHeaders) {
        Map<Object, Object> responseBuilder = new HashMap<>();
        responseBuilder.put("trackingNumber", pTrackingNumber);
        responseBuilder.put("serverResponse", "Pretend Response from FEDEX...");
        return responseBuilder;
    }

    private Object getResponseFromDHL(String pTrackingNumber, Optional<String> pParam, Map<String, String> pHeaders) {
        Map<Object, Object> responseBuilder = new HashMap<>();
        responseBuilder.put("trackingNumber", pTrackingNumber);
        responseBuilder.put("serverResponse", "Pretend response from DHL");
        return responseBuilder;
    }

    private Object getResponseFromUSPS(String pTrackingNumber, Optional<String> pParam, Map<String, String> pHeaders)
            throws IOException {
        Map<Object, Object> responseBuilder = new HashMap<>();
        XmlMapper mapper = new XmlMapper();
        responseBuilder.put("trackingNumber", pTrackingNumber);
        // responseBuilder.put("serverResponse", "USPS soap response... will need work
        // :D");
        String strRequest = "<TrackRequest USERID=\"015FROST5949\"><TrackID ID=\"9300120111404953153460\"></TrackID></TrackRequest>";
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://secure.shippingapis.com/ShippingAPI.dll?API=TrackV2&XML=" + strRequest)
                .method("GET", null).build();
        Response response = client.newCall(request).execute();
        String xmlString = mapper.writeValueAsString(response.body().string());
        responseBuilder.put("serverResponse", response.body().string());
        return responseBuilder;
    }

    private Map<Object, Object> getResponseFromUPS(String pTrackingNumber, final Optional<String> pParam,
            Map<String, String> pHeaders) throws IOException, InterruptedException, ParseException {
        HttpResponse<String> response;

        if (pHeaders.containsKey("accesslicensenumber")) {
            String strUrl = "https://onlinetools.ups.com/track/v1/details/" + pTrackingNumber + "?locale=en_US";
            String accessLicenseNumber = pHeaders.get("accesslicensenumber");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(strUrl))
                    .setHeader("Content-Type", "application/json").setHeader("Accept", "*/*")
                    .setHeader("AccessLicenseNumber", accessLicenseNumber).build();
            response = client.send(request, BodyHandlers.ofString());
        } else {
            throw new RuntimeException("Header does not include AccessLicenseNumber required by UPS.");
        }
        JSONParser parser = new JSONParser();
        Map<Object, Object> responseBuilder = new HashMap<>();
        responseBuilder.put("trackingNumber", pTrackingNumber);
        responseBuilder.put("serverResponse", (JSONObject) parser.parse(response.body()));
        return responseBuilder;
    }

}