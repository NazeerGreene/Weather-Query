package com.learn.WeatherApp.service;

import com.learn.WeatherApp.data.VisualCrossingWeatherFetcher;
import com.learn.WeatherApp.models.WeatherResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
public class VisualCrossingImpl implements WeatherService {
    // to build API request
    private final String baseUrl;
    private final String apiKey;

    @Autowired
    public VisualCrossingImpl(
            @Value("${weather.api.base-url}") String baseUrl,
            @Value("${weather.api.key}") String apiKey
    ) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public WeatherResponse atZipCode(@NonNull String zip) {
        return new VisualCrossingWeatherFetcher(baseUrl, apiKey)
                .onLocation(zip)
                .fetch();
    }

    public WeatherResponse atZipCodeBetweenDates (@NonNull String zip, @NonNull String start, String end) throws DateTimeParseException {
        LocalDate date1 = parseDate(start);
        LocalDate date2 = (end != null ? parseDate(end) : null);

        return new VisualCrossingWeatherFetcher(baseUrl, apiKey)
                .onLocation(zip)
                .betweenDates(date1, date2)
                .fetch();
    }

    private LocalDate parseDate(@NonNull String date) {
        return LocalDate.parse(date, VisualCrossingWeatherFetcher.dateTimeFormat);
    }
}

