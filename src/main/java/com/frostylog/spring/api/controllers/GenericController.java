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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.core.MediaType;
import javax.xml.stream.XMLStreamException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.frostylog.spring.api.models.TrackingData;
import com.frostylog.spring.api.models.temp.BarcodeAbstract;
import com.frostylog.spring.api.models.temp.ProductBarcodeDto;
import com.frostylog.spring.api.models.temp.SupplyBarcodeDto;
import com.frostylog.spring.api.models.usps.TrackResponse;

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

import okhttp3.OkHttpClient;
import okhttp3.Request;
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
            throws IOException, InterruptedException, ParseException, XMLStreamException {
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
                    responseBuilder.put("error: ", data.getCarrier().toLowerCase() + " is not a valid carrier.");
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
            throws IOException, XMLStreamException {

        if (pHeaders.containsKey("usps-userid")) {
            String userId = pHeaders.get("usps-userid");
            Map<Object, Object> responseBuilder = new HashMap<>();
            XmlMapper mapper = new XmlMapper();
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            responseBuilder.put("trackingNumber", pTrackingNumber);
            String strRequest = "<TrackRequest USERID=" + userId + "\"><TrackID ID=\"" + pTrackingNumber
                    + "\"></TrackID></TrackRequest>";
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                    .url("https://secure.shippingapis.com/ShippingAPI.dll?API=TrackV2&XML=" + strRequest)
                    .method("GET", null).build();
            ResponseBody responseBody = client.newCall(request).execute().body();
            InputStream inputStream = responseBody.byteStream();
            TrackResponse trackResponse = mapper.readValue(inputStream, TrackResponse.class);

            if (trackResponse.getTrackInfo() == null) {
                responseBuilder.put("serverResponse", (Optional.of(client.newCall(request).execute().body().string())));
                return responseBuilder;
            }
            responseBuilder.put("serverResponse", trackResponse);
            return responseBuilder;
        } else
            throw new RuntimeException("Header does not include USERID as required by USPS.");
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

    // http://localhost:8080/api/v1/test?type=product
    @RequestMapping(value = "/test", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public Object testStuff(@RequestBody LinkedHashMap<String, Object> test, @RequestParam String type)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        switch (type) {
            case "product":
                System.out.println("This is a product barcode dto...");
                // convert to the correct type and pass it to the barcode generic service....
                BarcodeAbstractService
                        .createBarcode(mapper.readValue(mapper.writeValueAsString(test), ProductBarcodeDto.class));
                break;
            case "supply":
                System.out.println("This is a supply barcode dto...");
                BarcodeAbstractService
                        .createBarcode(mapper.readValue(mapper.writeValueAsString(test), SupplyBarcodeDto.class));
                break;
            default:
                break;
        }

        return "test";

    }
}