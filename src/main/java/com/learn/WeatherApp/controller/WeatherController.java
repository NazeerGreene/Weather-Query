package com.learn.WeatherApp.controller;

import com.learn.WeatherApp.models.WeatherResponse;
import com.learn.WeatherApp.service.VisualCrossingImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class WeatherController {
    private final VisualCrossingImpl weatherService;

    public WeatherController(VisualCrossingImpl weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/zip/{zip}")
    public WeatherResponse getWeatherForZipCode(@PathVariable String zip) {
        WeatherResponse repsonse = weatherService.getByZipCode(zip);
        return repsonse;
    }
}
