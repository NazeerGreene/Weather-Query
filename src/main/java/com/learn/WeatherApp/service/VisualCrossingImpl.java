package com.learn.WeatherApp.service;

import com.learn.WeatherApp.models.WeatherResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VisualCrossingImpl {
    // to build API request
    private static final String requestUrl = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/%s?unitGroup=us&include=days&key=%s&contentType=json";
    private static final String apiKey = System.getenv("apiKey");

    // to send API request
    private RestTemplate restTemplate;

    @Autowired
    public VisualCrossingImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getByZipCode(@NonNull String zip) {
        String urlRequest = String.format(requestUrl, zip, apiKey);
        return restTemplate.getForObject(urlRequest, WeatherResponse.class);
    }
}
