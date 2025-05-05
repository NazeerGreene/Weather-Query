package com.learn.WeatherApp.service;

import com.learn.WeatherApp.data.VisualCrossingWeatherFetcher;
import com.learn.WeatherApp.models.VcWeatherResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Service layer for handling API request to Visual Crossing API
 */
@Service
public class VisualCrossingImpl implements WeatherService {
    // to build API request
    private final String apiKey;

    /**
     * Constructs a VisualCrossingImpl with the required API key.
     *
     * @param apiKey The API key
     */
    @Autowired
    public VisualCrossingImpl(
            @Value("${weather.api.key}") String apiKey
    ) {
        this.apiKey = apiKey;
    }

    /**
     * Fetches weather data according to location, which could be one of the following options:
     *
     * 1. Zip code
     * 2. City name, State
     * 3. longitude, latitude
     *
     * @param location The location to fetch weather data
     * @return VcWeatherResponse containing the corresponding data
     *
     * NOTE: This method caches the result using Spring's caching mechanism.
     */
    @Cacheable(value = "location-weather", key = "#location")
    public VcWeatherResponse atLocation(@NonNull String location) {
        return new VisualCrossingWeatherFetcher(apiKey)
                .onLocation(location)
                .fetch();
    }

    /**
     * Fetches weather data according to location at between some intervals.
     * The end date is optional; and if end is null, then only the start date will be considered
     * in the API request.
     * The start and end date should be the same date specified under {@link VisualCrossingWeatherFetcher#dateTimeFormat}.
     *
     * @param location The location to fetch.
     * @param start The start date of the requested interval.
     * @param end The end date for the interval requested; null if no end date needed, 16-day interval default.
     * @return VcWeatherResponse with the corresponding weather data between start, end.
     * @throws DateTimeParseException If start and end are not formatted properly.
     */
    public VcWeatherResponse atLocationBetweenDates (@NonNull String location, @NonNull String start, String end) throws DateTimeParseException {
        LocalDate date1 = parseDate(start);
        LocalDate date2 = (end != null ? parseDate(end) : null);

        return new VisualCrossingWeatherFetcher(apiKey)
                .onLocation(location)
                .betweenDates(date1, date2)
                .fetch();
    }

    private LocalDate parseDate(@NonNull String date) {
        return LocalDate.parse(date, VisualCrossingWeatherFetcher.dateTimeFormat);
    }
}

