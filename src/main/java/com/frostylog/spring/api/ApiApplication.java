package com.frostylog.spring.api;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Paths;
import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApiApplication.class, args);
		doRequest();
	}

	public static void doRequest() {
		String code = "1Z81RR270372827930";
		String strUrl = "https://onlinetools.ups.com/track/v1/details/" + code + "?locale=en_US";
		String accessLicenseNumber = "";
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(strUrl))
				.setHeader("Content-Type", "application/json").setHeader("Accept", "*/*")
				.setHeader("AccessLicenseNumber", accessLicenseNumber).build();
		client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept(System.out::println)
				.join();

	}

}
