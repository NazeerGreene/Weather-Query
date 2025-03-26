package com.learn.WeatherApp.controller;

import com.learn.WeatherApp.models.WeatherResponse;
import com.learn.WeatherApp.service.VisualCrossingImpl;
import com.learn.WeatherApp.service.WeatherService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // this annotation enables caching automatically
    // value -- the cache name where data is stored
    // key -- defines how the cache key is generated
    @Cacheable(value = "location", key = "#address")
    @GetMapping("/weather/location/{address}")
    public WeatherResponse forLocation(@PathVariable() String address) {
        WeatherResponse response = weatherService.atLocation(address);
        return response;
    }

    @GetMapping("/weather/location/{location}/{start}")
    public ResponseEntity<Object> forLocationAtStartDate(
            @PathVariable String location,
            @PathVariable String start
    ) {
        try {
            WeatherResponse response = weatherService.atLocationBetweenDates(location, start, null);
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format submitted, valid date format: yyyy-MM-dd");
        }
    }

    @GetMapping("/weather/location/{location}/{start}/{end}")
    public ResponseEntity<Object> forLocationBetweenDates(
            @PathVariable String location,
            @PathVariable String start,
            @PathVariable String end
    ) {
        try {
            WeatherResponse response = weatherService.atLocationBetweenDates(location, start, end);
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format submitted, valid date format: yyyy-MM-dd");
        }
    }
}
