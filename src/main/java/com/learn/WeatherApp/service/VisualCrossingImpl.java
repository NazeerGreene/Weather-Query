package com.learn.WeatherApp.service;

import com.learn.WeatherApp.models.WeatherResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class VisualCrossingImpl {
    // to build API request
    private static final String baseUrl = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private static final String apiKey = System.getenv("apiKey");

    // to send API request
    private final RestTemplate restTemplate;

    // formatting for weather API request
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public VisualCrossingImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse atZipCode(@NonNull String zip) {
        String urlRequest = String.format("%s/%s?unitGroup=us&include=days&key=%s&contentType=json", baseUrl, zip, apiKey);
        return restTemplate.getForObject(urlRequest, WeatherResponse.class);
    }

    public WeatherResponse atZipCodeBetweenDates (@NonNull String zip, @NonNull String start, String end) throws DateTimeParseException {
        LocalDate date1 = parseDate(start);
        LocalDate date2 = (end != null ? parseDate(end) : null);

        String urlRequest = date2 != null ?
                String.format("%s/%s/%s/%s?key=%s",baseUrl, zip, date1.toString(), date2.toString(), apiKey):
                String.format("%s/%s/%s?key=%s", baseUrl, zip, date1.toString(), apiKey);

        return restTemplate.getForObject(urlRequest, WeatherResponse.class);
    }

    private String buildUrlRequestAtZip(@NonNull String zip, LocalDate date1, LocalDate date2) {
        return null;
    }

    private LocalDate parseDate(@NonNull String date) {
        try {
            return LocalDate.parse(date, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        }
    }
}

