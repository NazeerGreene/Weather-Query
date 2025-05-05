package com.learn.WeatherApp.controller;

import com.learn.WeatherApp.models.VcWeatherResponse;
import com.learn.WeatherApp.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeParseException;

/**
 * REST Controller for handling weather-related requests.
 * This controller provides endpoints for retrieving weather data based on location
 * and optional date parameters. It utilizes caching to improve performance.
 */
@RestController
public class WeatherController {
    private final WeatherService weatherService;

    /**
     * Constructs a WeatherController with the required WeatherService dependency.
     *
     * @param weatherService The weather service used to fetch weather data.
     */
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Retrieves weather data for a given location.
     *
     * @param location The location (city, state, or coordinates) for which weather data is requested.
     * @return A {@link VcWeatherResponse} containing the weather data.
     */
    @GetMapping("/weather/location/{location}")
    public VcWeatherResponse forLocation(@PathVariable String location) {
        VcWeatherResponse response = weatherService.atLocation(location);
        return response;
    }

    /**
     * Retrieves weather data for a given location at a specific start date.
     * If an invalid date format is provided, a 400 BAD REQUEST response is returned.
     *
     * @param location The location for which weather data is requested.
     * @param start    The start date in the format "yyyy-MM-dd".
     * @return A {@link ResponseEntity} containing either the weather data
     *         or an error message if the date format is invalid.
     */
    @GetMapping("/weather/location/{location}/{start}")
    public ResponseEntity<Object> forLocationAtStartDate(
            @PathVariable String location,
            @PathVariable String start
    ) {
        try {
            VcWeatherResponse response = weatherService.atLocationBetweenDates(location, start, null);
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format submitted, valid date format: yyyy-MM-dd");
        }
    }

    /**
     * Retrieves weather data for a given location between a start and end date.
     * If an invalid date format is provided, a 400 BAD REQUEST response is returned.
     *
     * @param location The location for which weather data is requested.
     * @param start    The start date in the format "yyyy-MM-dd".
     * @param end      The end date in the format "yyyy-MM-dd".
     * @return A {@link ResponseEntity} containing either the weather data
     *         or an error message if the date format is invalid.
     */
    @GetMapping("/weather/location/{location}/{start}/{end}")
    public ResponseEntity<Object> forLocationBetweenDates(
            @PathVariable String location,
            @PathVariable String start,
            @PathVariable String end
    ) {
        try {
            VcWeatherResponse response = weatherService.atLocationBetweenDates(location, start, end);
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format submitted, valid date format: yyyy-MM-dd");
        }
    }
}

