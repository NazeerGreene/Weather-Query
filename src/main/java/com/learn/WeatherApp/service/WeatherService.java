package com.learn.WeatherApp.service;

import com.learn.WeatherApp.models.VcWeatherResponse;

/**
 * The interface that any service layer fetchers should conform to.
 */
public interface WeatherService {
    VcWeatherResponse atLocation(String zip);
    VcWeatherResponse atLocationBetweenDates(String zip, String start, String end);
}
