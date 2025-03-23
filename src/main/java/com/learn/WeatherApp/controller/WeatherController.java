package com.learn.WeatherApp.controller;

import com.learn.WeatherApp.models.WeatherResponse;
import com.learn.WeatherApp.service.VisualCrossingImpl;
import com.learn.WeatherApp.service.WeatherService;
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

    @GetMapping("/weather/zip/{zip}")
    public ResponseEntity<WeatherResponse> forZipCode(@PathVariable() String zip) {
        WeatherResponse response = weatherService.atZipCode(zip);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/weather/zip/{zip}/{start}")
    public ResponseEntity<Object> forZipCodeBetweenDates(
            @PathVariable String zip,
            @PathVariable String start
    )
    {
        try {
            WeatherResponse response = weatherService.atZipCodeBetweenDates(zip, start, null);
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Date format: yyyy-MM-dd");
        }
    }

    @GetMapping("/weather/zip/{zip}/{start}/{end}")
    public ResponseEntity<Object> forZipCodeBetweenDates(
            @PathVariable String zip,
            @PathVariable String start,
            @PathVariable String end
    )
    {
        try {
            WeatherResponse response = weatherService.atZipCodeBetweenDates(zip, start, end);
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Date format: yyyy-MM-dd");
        }
    }
}
